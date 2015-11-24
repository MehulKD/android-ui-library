package com.github.badoualy.ui.adapter.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 A CursorAdapter that can be shuffled without making any supplementary database query, when overriding, make sure you use
 map/unmap methods when accessing items by their position
 */
public abstract class ShuffleCursorAdapter extends CursorAdapter {

    /**
     A mapping list that map the adapter position with the cursor position so that getItem(position) =
     cursor.moveToPosition(mapList.get(position));
     */
    private List<Integer> mapList = new ArrayList<>();

    public ShuffleCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        remap();
    }

    @TargetApi(11)
    public ShuffleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        remap();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(map(position), convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(map(position), convertView, parent);
    }

    @Override
    public Cursor getItem(int position) {
        return (Cursor) super.getItem(map(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(map(position));
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