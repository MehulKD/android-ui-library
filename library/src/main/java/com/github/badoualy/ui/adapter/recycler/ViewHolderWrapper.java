package com.github.badoualy.ui.adapter.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 A simple class that will wrap a view V as a Recycler ViewHolder using the interface Bindable

 @param <T> collection type
 @param <V> view type (must be a View and a Bindable of T) */
public class ViewHolderWrapper<T, V extends View & ArrayRecyclerAdapter.Bindable<T>> extends RecyclerView.ViewHolder implements ArrayRecyclerAdapter.Bindable<T> {

    private V view;

    public ViewHolderWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

    @Override
    public void bind(T item) {
        view.bind(item);
    }
}
