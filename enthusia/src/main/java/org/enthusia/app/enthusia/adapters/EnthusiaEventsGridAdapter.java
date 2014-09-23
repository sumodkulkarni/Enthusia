package org.enthusia.app.enthusia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaEvents;
import org.enthusia.app.ui.MaterialRippleLayout;

public class EnthusiaEventsGridAdapter extends BaseAdapter {

    private AdapterView.OnItemClickListener listener;

    public EnthusiaEventsGridAdapter(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
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
        DynamicHeightImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enthusia_grid_item_events, viewGroup, false);
            holder.imageView = (DynamicHeightImageView) view.findViewById(R.id.enthusia_events_list_item_event_image);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_events_list_item_event_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(EnthusiaEvents.drawables[i]);
        if (i == 18 || i == 20)
            holder.textView.setText("");
        else
            holder.textView.setText(EnthusiaEvents.events[i]);

        ((MaterialRippleLayout) view).getChildView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick((AdapterView) viewGroup, v, i, getItemId(i));
            }
        });

        return view;
    }

}
