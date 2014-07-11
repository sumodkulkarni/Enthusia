package org.enthusia.app.enthusia.adapters;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;

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
        DynamicHeightImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enthusia_list_item_events, viewGroup, false);
            holder.imageView = (DynamicHeightImageView) view.findViewById(R.id.enthusia_events_list_item_event_image);
            holder.textView = (TextView) view.findViewById(R.id.enthusia_events_list_item_event_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(EnthusiaEvents.drawables[i]);
//        holder.imageView.setImageBitmap(toGrayscale(BitmapFactory.decodeResource(viewGroup.getResources(), EnthusiaEvents.drawables[i])));
        holder.textView.setText(EnthusiaEvents.events[i]);
        return view;
    }

//    public Bitmap toGrayscale(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }
}
