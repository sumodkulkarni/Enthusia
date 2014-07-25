package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.enthusia.app.R;

public class EnthusiaEventsEventHeadAdapter extends BaseAdapter {

    private final Context context;
    private final String[] eventHeads;

    public EnthusiaEventsEventHeadAdapter (Context context, String[] eventHeads) {
        this.context = context;
        this.eventHeads = eventHeads;
    }

    @Override
    public String getItem(int i) {
        return eventHeads[i];
    }

    @Override
    public long getItemId(int i) {
        return eventHeads[i].hashCode();
    }

    @Override
    public int getCount() {
        return eventHeads.length;
    }

    private final static class ViewHolder {
        TextView name;
        TextView number;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_event_head, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.enthusia_list_item_event_head_name);
            holder.number = (TextView) view.findViewById(R.id.enthusia_list_item_event_head_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(eventHeads[i].split(":")[0]);
        holder.number.setText(eventHeads[i].split(":")[1].replace(" ", ""));
        return view;
    }
}
