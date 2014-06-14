package org.enthusia.app.enthusia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaCommittee;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class EnthusiaStickyHeaderAdapter extends ArrayAdapter<EnthusiaCommittee> implements StickyListHeadersAdapter {


    private Context context;
    private String[] mHeaders;

    public EnthusiaStickyHeaderAdapter(Context context, ArrayList<EnthusiaCommittee> mItems, String[] mHeaders) {
        super(context, R.layout.enthusia_list_item_event_head, mItems);
        this.context = context;
        this.mHeaders = mHeaders;
    }

    private static final class ViewHolder {
        TextView name;
        TextView number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_event_head, parent, false);
            holder.name =  (TextView) convertView.findViewById(R.id.enthusia_list_item_event_head_name);
            holder.number = (TextView) convertView.findViewById(R.id.enthusia_list_item_event_head_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).getName().split(":")[0]);
        holder.number.setText(getItem(position).getName().split(":")[1].replace(" ", ""));
        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getPosition();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private static final class HeaderViewHolder {
        TextView textView;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {

        HeaderViewHolder holder;

        if (view == null) {
            holder = new HeaderViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.enthusia_list_item_headers, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_list_item_header);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        holder.textView.setText(mHeaders[getItem(i).getPosition()]);
        return view;
    }
}
