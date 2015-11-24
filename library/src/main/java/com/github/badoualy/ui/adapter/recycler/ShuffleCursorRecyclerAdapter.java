package com.github.badoualy.ui.adapter.recycler;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 A CursorAdapter that can be shuffled without making any supplementary database query, when overriding, make sure you use
 map/unmap methods when accessing items by their position

 @param <T>  a model class representing cursor values
 @param <VH> ViewHolder type */
public abstract class ShuffleCursorRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends CursorRecyclerAdapter<T, VH> {

    /**
     A mapping list that map the adapter position with the cursor position so that getItem(position) =
     cursor.moveToPosition(mapList.get(position));
     */
    private List<Integer> mapList = new ArrayList<>();

    public ShuffleCursorRecyclerAdapter(Context context, Cursor cursor, ItemFactory<T> factory) {
        super(context, cursor, factory);
        remap();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(map(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(map(position));
    }

    @Override
    public T getItem(int position) {
        return super.getItem(map(position));
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor c = super.swapCursor(newCursor);
        remap();
        return c;
    }

    /**
     @param position position in the adapter
     @return real position of the item in the cursor
     */
    protected final int map(int position) {
        return position < mapList.size() ? mapList.get(position) : 0;
    }

    /**
     @param position real position in the cursor
     @return position in the adapter
     */
    protected final int unmap(int position) {
        return mapList.indexOf(position);
    }

    private void remap() {
        mapList.clear();
        if (getCursor() != null) {
            for (int i = 0; i < getCursor().getCount(); i++)
                mapList.add(i);
        }
    }

    /** Reorder the adapter to the original order */
    public void sort() {
        remap();
    }

    /** Shuffle the adapter to a random order */
    public void shuffle() {
        Collections.shuffle(mapList);
    }
}