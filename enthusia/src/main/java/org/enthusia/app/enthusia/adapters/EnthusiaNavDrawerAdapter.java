package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaNavDrawerItem;

import java.util.ArrayList;

public class EnthusiaNavDrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EnthusiaNavDrawerItem> mItems;

    public EnthusiaNavDrawerAdapter(Context context, ArrayList<EnthusiaNavDrawerItem> mItems) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public EnthusiaNavDrawerItem getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_nav_drawer, viewGroup, false);
            holder.imageView = (ImageView) view.findViewById(R.id.enthusia_list_item_nav_drawer_icon);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_list_item_nav_drawer_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(mItems.get(i).getTitle());
        holder.imageView.setImageResource(mItems.get(i).getIcon());

        if (getItem(i).isSelected()) {
            holder.imageView.setColorFilter(new PorterDuffColorFilter(viewGroup.getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_IN));
            holder.textView.setTextColor(viewGroup.getResources().getColor(R.color.app_color));
        } else {
            holder.imageView.setColorFilter(null);
            holder.textView.setTextColor(viewGroup.getResources().getColor(R.color.black));
        }

        return view;
    }

}
