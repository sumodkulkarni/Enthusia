package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopixl.pixlui.components.textview.TextView;
import com.nhaarman.listviewanimations.ArrayAdapter;
import org.enthusia.app.R;
import org.enthusia.app.model.PushMessage;

import java.util.ArrayList;

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
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_news, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_list_item_news_message);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (getItem(i).isRead()) {
            holder.textView.setTextColor(Color.parseColor("#6f6f6f"));
            holder.textView.setBackgroundResource(R.drawable.enthusia_news_background_read);
        } else {
            holder.textView.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.textView.setBackgroundResource(R.drawable.enthusia_news_background_unread);
        }
        holder.textView.setPadding(10,10,10,10);
        holder.textView.setText(getItem(i).getMessage());

        return view;
    }
}
