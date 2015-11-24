package com.github.badoualy.ui.adapter.recycler;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;

/**
 A RecyclerAdapter that uses a cursor as a collection
 Original code from: https://github.com/skyfishjy

 @param <T>  a model class representing cursor values
 @param <VH> ViewHolder type */
public abstract class CursorRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    /** A mapping table, to avoid recreating object from cursor every time */
    private LongSparseArray<T> objectMap;
    private ItemFactory<T> factory;

    public CursorRecyclerAdapter(Context context, Cursor cursor, ItemFactory<T> factory) {
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        setHasStableIds(true);
        objectMap = new LongSparseArray<>(cursor != null ? cursor.getCount() : 10);
        this.factory = factory;
    }

    public final Cursor getCursor() {
        return mCursor;
    }

    public final Context getContext() {
        return mContext;
    }

    @Override
    public final int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (!mDataValid)
            throw new IllegalStateException("This should only be called when the cursor is valid");
        if (!mCursor.moveToPosition(position))
            throw new IllegalStateException("Couldn't move cursor to position " + position);

        return mCursor.getLong(mRowIdColumn);
    }

    public T getItem(int position) {
        if (!mDataValid)
            throw new IllegalStateException("This should only be called when the cursor is valid");
        if (!mCursor.moveToPosition(position))
            throw new IllegalStateException("Couldn't move cursor to position " + position);

        long id = getItemId(position);
        T item = objectMap.get(id);
        if (item == null) {
            item = onCreateItem(mCursor);
            objectMap.put(id, item);
        }

        return item;
    }

    /** @return a model object representing the cursor, will return the cursor itself by default */
    public T onCreateItem(Cursor cursor) {
        return factory.newInstance(cursor);
    }

    @Override
    public final void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, T item, int position);

    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        if (!mDataValid)
            throw new IllegalStateException("This should only be called when the cursor is valid");
        if (!mCursor.moveToPosition(position))
            throw new IllegalStateException("Couldn't move cursor to position " + position);
        onBindViewHolder(viewHolder, getItem(position), position);
    }

    /**
     Change the underlying cursor to a new cursor. If there is an existing cursor it will be closed.

     @param cursor new cursor
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     Swap in a new Cursor, returning the old Cursor.  Unlike {@link #changeCursor(Cursor)}, the returned old Cursor is
     <em>not</em> closed.

     @param newCursor new cursor
     @return the old cursor or null if none
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    public final int getRowIdColumn() {
        return mRowIdColumn;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    public interface ItemFactory<T> {
        T newInstance(Cursor cursor);
    }
}
