package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;

import org.enthusia.app.R;

import java.util.ArrayList;

public class EnthusiaSponsorsAdapter extends ArrayAdapter<Integer> {

    public EnthusiaSponsorsAdapter(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.enthusia_grid_item_sponsors, objects);
    }

    private static class ViewHolder {
        DynamicHeightImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enthusia_grid_item_sponsors, parent, false);
            holder.imageView = (DynamicHeightImageView) convertView.findViewById(R.id.enthusia_events_grid_item_sponsor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(getItem(position));
        return convertView;
    }
}
