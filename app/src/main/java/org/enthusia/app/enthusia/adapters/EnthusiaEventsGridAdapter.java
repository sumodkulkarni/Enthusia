package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

public class EnthusiaEventsGridAdapter extends BaseAdapter {

    private Context context;

    public EnthusiaEventsGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return EnthusiaEvents.drawables.length;
    }

    @Override
    public Integer getItem (int i) {
        return EnthusiaEvents.drawables[i];
    }

    @Override
    public long getItemId(int i) {
        return Integer.valueOf(EnthusiaEvents.drawables[i]).hashCode();
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_events, viewGroup, false);
            holder.imageView = (ImageView) view.findViewById(R.id.enthusia_events_list_item_event_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(EnthusiaEvents.drawables[i]);
        return view;
    }
}
