package org.enthusia.app.enthusia.ui;

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

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaPointsTable;

import java.util.ArrayList;
import java.util.Collections;

public class EnthusiaPointsTableDialog extends Dialog {

    private Activity activity;
    private ArrayList<EnthusiaPointsTable> tableData;

    public final static String PREF_POINT_TABLE = "org.enthusia.app.enthusia.points";
    public final static String PREF_POINTS = "pref_points_";


    public EnthusiaPointsTableDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.tableData = new ArrayList<EnthusiaPointsTable>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        for (int i=0; i < getContext().getResources().getStringArray(R.array.enthusia_departments).length; i++) {
            this.tableData.add(new EnthusiaPointsTable(
                    getContext().getResources().getStringArray(R.array.enthusia_departments)[i],
                    getPoints(getContext().getResources().getStringArray(R.array.enthusia_departments)[i])
            ));
        }
        Collections.sort(this.tableData);

        setContentView(R.layout.enthusia_dialog_points_table);
        TableLayout pointsTable = (TableLayout) findViewById(R.id.enthusia_dialog_points_table_table);
        for (int i=0; i < this.tableData.size(); i++) {
            pointsTable.addView(getHorizontalDivider());

            TableRow row = new TableRow(activity);
            row.addView(getVerticalDivider());
            for (int j=0; j < 2; j++) {
                row.addView( (j % 2 == 0 ? getDepartmentView(i) : getPointView(i)));
                row.addView(getVerticalDivider());
            }

            pointsTable.addView(row);
            pointsTable.addView(getHorizontalDivider());
        }
    }

    private View getHorizontalDivider() {
        View v = new View(activity);
        v.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1);
        v.setLayoutParams(params);
        return v;
    }

    private View getVerticalDivider() {
        View v = new View(activity);
        TableRow.LayoutParams params = new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(params);
        v.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        return v;
    }

    private TextView getPointView(int i) {
        TextView textView = getView();
        textView.setText(this.tableData.get(i).getPoints() + "");
        textView.setGravity(Gravity.RIGHT);
        return textView;
    }

    private TextView getDepartmentView(int i) {
        TextView textView = getView();
        textView.setText(this.tableData.get(i).getDepartment());
        textView.setPadding(10,0,0,10);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        textView.setLayoutParams(params);
        return textView;
    }

    private TextView getView() {
        TextView textView = new TextView(activity);
        textView.setTextSize(30.0f);
        textView.setPadding(10,0,0,10);
        textView.setTextColor(activity.getResources().getColor(R.color.black));
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        textView.setLayoutParams(params);
        return textView;
    }

    @SuppressWarnings("defaultlocale")
    private int getPoints(String department) {
        return getContext().getSharedPreferences(PREF_POINT_TABLE, Context.MODE_PRIVATE).getInt(PREF_POINTS + department.toLowerCase(), 0);
    }

}
