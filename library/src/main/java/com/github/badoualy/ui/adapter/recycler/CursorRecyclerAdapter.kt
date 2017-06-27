package com.github.badoualy.ui.adapter.recycler

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.support.v4.util.LongSparseArray
import android.support.v7.widget.RecyclerView

/**
 * A RecyclerAdapter that uses a cursor as a collection
 * Original code from: https://github.com/skyfishjy

 * @param <T>  a model class representing cursor values
 * @param <VH> ViewHolder type */
abstract class CursorRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(val context: Context, cursor: Cursor?, private val factory: ItemFactory<T>) : RecyclerView.Adapter<VH>() {

    var cursor: Cursor? = null
        private set

    private var mDataValid: Boolean = false

    var rowIdColumn: Int = 0
        private set

    private val mDataSetObserver: DataSetObserver?

    /** A mapping table, to avoid recreating object from cursor every time  */
    private val objectMap: LongSparseArray<T>

    init {
        this.cursor = cursor
        mDataValid = cursor != null
        rowIdColumn = if (mDataValid) this.cursor!!.getColumnIndex("_id") else -1
        mDataSetObserver = NotifyingDataSetObserver()
        if (this.cursor != null) {
            this.cursor!!.registerDataSetObserver(mDataSetObserver)
        }
        setHasStableIds(true)
        objectMap = LongSparseArray<T>(cursor?.count ?: 10)
    }

    override fun getItemCount(): Int {
        if (mDataValid && cursor != null) {
            return cursor!!.count
        }
        return 0
    }

    override fun getItemId(position: Int): Long {
        if (!mDataValid)
            throw IllegalStateException("This should only be called when the cursor is valid")
        if (!cursor!!.moveToPosition(position))
            throw IllegalStateException("Couldn't move cursor to position " + position)

        return cursor!!.getLong(rowIdColumn)
    }

    open fun getItem(position: Int): T {
        if (!mDataValid)
            throw IllegalStateException("This should only be called when the cursor is valid")
        if (!cursor!!.moveToPosition(position))
            throw IllegalStateException("Couldn't move cursor to position " + position)

        val id = getItemId(position)
        var item: T? = objectMap.get(id)
        if (item == null) {
            item = onCreateItem(cursor!!)
            objectMap.put(id, item)
        }

        return item!!
    }

    /** @return a model object representing the cursor, will return the cursor itself by default
     */
    fun onCreateItem(cursor: Cursor) = factory.newInstance(cursor)

    override final fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    abstract fun onBindViewHolder(viewHolder: VH, item: T, position: Int)

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (!mDataValid)
            throw IllegalStateException("This should only be called when the cursor is valid")
        if (!cursor!!.moveToPosition(position))
            throw IllegalStateException("Couldn't move cursor to position " + position)
        onBindViewHolder(viewHolder, getItem(position), position)
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be closed.

     * @param cursor new cursor
     */
    fun changeCursor(cursor: Cursor) {
        val old = swapCursor(cursor)
        old?.close()
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike [.changeCursor], the returned old Cursor is
     * *not* closed.

     * @param newCursor new cursor
     * @return the old cursor or null if none
     */
    open fun swapCursor(newCursor: Cursor): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val oldCursor = cursor
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver)
        }
        cursor = newCursor
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor!!.registerDataSetObserver(mDataSetObserver)
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id")
            mDataValid = true
            notifyDataSetChanged()
        } else {
            rowIdColumn = -1
            mDataValid = false
            notifyDataSetChanged()
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor
    }

    private inner class NotifyingDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            mDataValid = false
            notifyDataSetChanged()
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    interface ItemFactory<T> {
        fun newInstance(cursor: Cursor): T
    }
}
