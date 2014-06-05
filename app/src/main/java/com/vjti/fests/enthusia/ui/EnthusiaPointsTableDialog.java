package com.vjti.fests.enthusia.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vjti.fests.R;

public class EnthusiaPointsTableDialog extends Dialog {

    private Activity activity;
    private TableLayout pointsTable;

    private final static String PREF_POINT_TABLE = "com.vjti.fets.enthusia.points";
    private final static String PREF_POINTS = "pref_points_";


    public EnthusiaPointsTableDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.enthusia_dialog_points_table);
        pointsTable = (TableLayout) findViewById(R.id.enthusia_dialog_points_table_table);
        for (int i=0; i < getContext().getResources().getStringArray(R.array.enthusia_departments).length; i++) {
            pointsTable.addView(getHorizontalDivider());

            TableRow row = new TableRow(activity);
            row.addView(getVerticalDivider());
            for (int j=0; j < 2; j++) {
                row.addView( (j % 2 == 0 ? getDepartmentView(i, j) : getPointView(i, j)));
                row.addView(getVerticalDivider());
            }

            pointsTable.addView(row);
            pointsTable.addView(getHorizontalDivider());
        }
    }

    private View getHorizontalDivider() {
        View v = new View(activity);
        v.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1);
        v.setLayoutParams(params);
        return v;
    }

    private View getVerticalDivider() {
        View v = new View(activity);
        TableRow.LayoutParams params = new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(params);
        v.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
        return v;
    }

    private TextView getPointView(int i, int j) {
        TextView textView = getView(j);
        textView.setText(getPoints(getContext().getResources().getStringArray(R.array.enthusia_departments)[i]) + "");
        textView.setGravity(Gravity.RIGHT);
        return textView;
    }

    private TextView getDepartmentView(int i, int j) {
        TextView textView = getView(j);
        textView.setText(getContext().getResources().getStringArray(R.array.enthusia_departments)[i]);
        return textView;
    }

    private TextView getView(int i) {
        TextView textView = new TextView(activity);
        textView.setTextSize(30.0f);
        return textView;
    }

    @SuppressWarnings("defaultlocale")
    private float getPoints(String department) {
        department = department.toLowerCase();
        return getContext().getSharedPreferences(PREF_POINT_TABLE, Context.MODE_PRIVATE).getFloat(PREF_POINTS + department, 0.0f);
    }

}
