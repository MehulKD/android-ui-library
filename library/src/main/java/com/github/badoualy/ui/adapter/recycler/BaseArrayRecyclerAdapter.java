package com.github.badoualy.ui.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A custom RecyclerAdapter that handle a list of items as a collection (much like ListView's ArrayAdapter)
 *
 * @param <T> Collection type
 * @param <V> ViewHolder type
 */
public abstract class BaseArrayRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private Context context;
    private List<T> items;

    public BaseArrayRecyclerAdapter(Context context, List<? extends T> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
    }

    public BaseArrayRecyclerAdapter(Context context, T[] items) {
        this(context, Arrays.asList(items));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Context getContext() {
        return context;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public T getItemOrNull(int position) {
        if (position < 0 || position >= getItemCount())
            return null;
        return items.get(position);
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    public void clear() {
        items.clear();
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public T remove(int location) {
        return items.remove(location);
    }

    public boolean removeAll(Collection<? extends T> collection) {
        return items.removeAll(collection);
    }

    public void add(int location, T object) {
        items.add(location, object);
    }

    public boolean addAll(int location, Collection<? extends T> collection) {
        return items.addAll(location, collection);
    }

    public boolean addAll(Collection<? extends T> collection) {
        return items.addAll(collection);
    }

    public void add(T item) {
        items.add(item);
    }

    public boolean containsAll(Collection<? extends T> collection) {
        return items.containsAll(collection);
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public int indexOf(T item) {
        return items.indexOf(item);
    }

    public void sort() {
        List<? extends Comparable> items = (List<? extends Comparable>) this.items;
        Collections.sort(items);
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(items, comparator);
    }

    /** @return true if the item decorator should draw a divider for the item at the given position */
    public boolean hasDivider(int position) {
        return true;
    }
}