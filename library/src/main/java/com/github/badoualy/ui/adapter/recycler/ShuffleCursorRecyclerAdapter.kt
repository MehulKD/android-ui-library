package com.github.badoualy.ui.adapter.recycler


import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import java.util.*

/**
 * A CursorAdapter that can be shuffled without making any supplementary database query, when overriding, make sure you use
 * map/unmap methods when accessing items by their position

 * @param <T>  a model class representing cursor values
 * @param <VH> ViewHolder type
</VH></T> */
abstract class ShuffleCursorRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(context: Context, cursor: Cursor, factory: CursorRecyclerAdapter.ItemFactory<T>)
    : CursorRecyclerAdapter<T, VH>(context, cursor, factory) {

    /**
     * A mapping list that map the adapter position with the cursor position so that getItem(position) =
     * cursor.moveToPosition(mapList.get(position));
     */
    private val mapList = ArrayList<Int>()

    init {
        remap()
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(map(position))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(map(position))
    }

    override fun getItem(position: Int): T {
        return super.getItem(map(position))
    }

    override fun swapCursor(newCursor: Cursor): Cursor? {
        val c = super.swapCursor(newCursor)
        remap()
        return c
    }

    /**
     * @param position position in the adapter
     * @return real position of the item in the cursor
     */
    protected fun map(position: Int): Int {
        return if (position < mapList.size) mapList[position] else 0
    }

    /**
     * @param position real position in the cursor
     * @return position in the adapter
     */
    protected fun unmap(position: Int): Int {
        return mapList.indexOf(position)
    }

    private fun remap() {
        mapList.clear()
        if (cursor != null) {
            mapList += 0..(cursor!!.count - 1)
        }
    }

    /** Reorder the adapter to the original order  */
    fun sort() {
        remap()
    }

    /** Shuffle the adapter to a random order  */
    fun shuffle() {
        Collections.shuffle(mapList)
    }
}