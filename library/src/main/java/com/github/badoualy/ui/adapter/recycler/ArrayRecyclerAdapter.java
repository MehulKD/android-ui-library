package com.github.badoualy.ui.adapter.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 A custom RecyclerAdapter that handle almost everything, the subclasses only needs to override onCreateItemView to
 inflate the xml. Use this class when your adapter has no specific behaviors (header, footer, different viewTypes).

 @param <T> Collection type
 @param <V> View class, the view must extends View and implement the interface Bindable */
public abstract class ArrayRecyclerAdapter<T, V extends View & ArrayRecyclerAdapter.Bindable<T>> extends BaseArrayRecyclerAdapter<T, ViewHolderWrapper<T, V>> {

    public ArrayRecyclerAdapter(Context context, List<? extends T> items) {
        super(context, items);
    }

    public ArrayRecyclerAdapter(Context context, T[] items) {
        super(context, Arrays.asList(items));
    }

    @Override
    public ViewHolderWrapper<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderWrapper<>(onCreateItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(ViewHolderWrapper<T, V> holder, int position) {
        holder.bind(getItem(position));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public interface Bindable<T> {
        void bind(T item);
    }
}