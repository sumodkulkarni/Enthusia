package org.enthusia.app.enthusia.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.model.EnthusiaPointsTable;

import java.util.ArrayList;
import java.util.Collections;

public class EnthusiaPointsTableDialog extends DialogFragment {

    private ArrayList<EnthusiaPointsTable> tableData;
    private TableLayout pointsTable;

    public final static String PREF_POINT_TABLE = "org.enthusia.app.enthusia.points";
    public final static String PREF_POINTS = "pref_points_";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AppTheme_Dialog;
        View v = inflater.inflate(R.layout.enthusia_dialog_points_table, container, false);
        pointsTable = (TableLayout) v.findViewById(R.id.enthusia_dialog_points_table_table);
        return v;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null)
            tableData = new ArrayList<EnthusiaPointsTable>();
        else
            tableData = (ArrayList<EnthusiaPointsTable>) savedInstanceState.getSerializable("points");

        if (savedInstanceState == null)
            for (int i=0; i < getActivity().getResources().getStringArray(R.array.enthusia_departments).length; i++) {
                this.tableData.add(new EnthusiaPointsTable(
                        getActivity().getResources().getStringArray(R.array.enthusia_departments)[i],
                        getPoints(getActivity().getResources().getStringArray(R.array.enthusia_departments)[i])
                ));
            }
        Collections.sort(this.tableData);

        for (int i=0; i < this.tableData.size(); i++) {
            pointsTable.addView(getHorizontalDivider());

            TableRow row = new TableRow(getActivity());
            row.addView(getVerticalDivider());
            for (int j=0; j < 2; j++) {
                row.addView( (j % 2 == 0 ? getDepartmentView(i) : getPointView(i)));
                row.addView(getVerticalDivider());
            }

            pointsTable.addView(row);
            pointsTable.addView(getHorizontalDivider());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("points", tableData);
    }

    private View getHorizontalDivider() {
        View v = new View(getActivity());
        v.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1);
        v.setLayoutParams(params);
        return v;
    }

    private View getVerticalDivider() {
        View v = new View(getActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(params);
        v.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        return v;
    }

    private TextView getPointView(int i) {
        TextView textView = getBasicTextView();
        textView.setText(this.tableData.get(i).getPoints() + "");
        textView.setGravity(Gravity.RIGHT);
        return textView;
    }

    private TextView getDepartmentView(int i) {
        TextView textView = getBasicTextView();
        textView.setText(this.tableData.get(i).getDepartment());
        textView.setPadding(10,0,0,10);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        textView.setLayoutParams(params);
        return textView;
    }

    private TextView getBasicTextView() {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(30.0f);
        textView.setPadding(10,0,0,10);
        textView.setTextColor(getActivity().getResources().getColor(R.color.black));
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        textView.setLayoutParams(params);
        return textView;
    }

    @SuppressWarnings("defaultlocale")
    private int getPoints(String department) {
        return getActivity().getSharedPreferences(PREF_POINT_TABLE, Context.MODE_PRIVATE).getInt(PREF_POINTS + department.toLowerCase(), 0);
    }

}
