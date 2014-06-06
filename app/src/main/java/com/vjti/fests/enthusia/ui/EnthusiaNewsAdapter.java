package com.vjti.fests.enthusia.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.vjti.fests.R;
import com.vjti.fests.model.PushMessage;

import java.util.ArrayList;
import java.util.List;

public class EnthusiaNewsAdapter extends ArrayAdapter<PushMessage> {

    private Context context;

    public EnthusiaNewsAdapter (Context context, ArrayList<PushMessage> mItems) {
        super(mItems);
        this.context = context;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private static final class ViewHolder {
        TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_lsit_item_news, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_list_item_news_message);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(getItem(i).getMessage());

        if (getItem(i).isRead()) {
            view.setBackgroundColor(context.getResources().getColor(R.color.read));
            holder.textView.setTypeface(null, Typeface.NORMAL);
        } else {
            view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.textView.setTypeface(null, Typeface.BOLD);
        }

        return view;
    }

    public List<PushMessage> getItems() {
        return super.mItems;
    }
}
