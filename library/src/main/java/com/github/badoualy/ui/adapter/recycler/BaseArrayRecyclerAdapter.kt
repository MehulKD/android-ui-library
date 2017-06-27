package com.github.badoualy.ui.adapter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import java.util.*

abstract class BaseArrayRecyclerAdapter<T, V : RecyclerView.ViewHolder>(val context: Context, items: Collection<T>) : RecyclerView.Adapter<V>() {

    private val items: MutableList<T> = ArrayList(items)

    constructor(context: Context, items: Array<T>) : this(context, Arrays.asList(*items)) {}

    override fun getItemCount() = items.size

    fun getItem(position: Int) = items[position]

    fun getItemOrNull(position: Int) =
            if (position < 0 || position >= itemCount) null
            else getItem(position)

    fun isEmpty() = itemCount == 0

    fun isNotEmpty() = itemCount != 0

    fun clear() = items.clear()

    fun remove(item: T) = items.remove(item)

    fun remove(location: Int) = items.removeAt(location)

    fun removeAll(collection: Collection<T>) = items.removeAll(collection)

    fun add(location: Int, item: T) = items.add(location, item)

    fun addAll(location: Int, collection: Collection<T>) = items.addAll(location, collection)

    fun addAll(collection: Collection<T>) = items.addAll(collection)

    fun add(item: T) = items.add(item)

    fun containsAll(collection: Collection<T>) = items.containsAll(collection)

    operator fun contains(item: T) = items.contains(item)

    fun indexOf(item: T) = items.indexOf(item)

    fun sort(comparator: Comparator<T>) {
        Collections.sort(items, comparator)
    }

    fun getItems() = ArrayList(items)
}