package org.enthusia.app.enthusia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

public class EnthusiaEventsGridAdapter extends BaseAdapter {

    public EnthusiaEventsGridAdapter() {}

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
        TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enthusia_list_item_events, viewGroup, false);
            holder.imageView = (ImageView) view.findViewById(R.id.enthusia_events_list_item_event_image);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_events_list_item_event_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(EnthusiaEvents.drawables[i]);
        holder.textView.setText(EnthusiaEvents.events[i]);
        return view;
    }
}
