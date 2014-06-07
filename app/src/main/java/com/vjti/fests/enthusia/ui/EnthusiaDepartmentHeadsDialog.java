package com.vjti.fests.enthusia.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.vjti.fests.R;
import com.vjti.fests.enthusia.adapters.EnthusiaStickyHeaderAdapter;
import com.vjti.fests.enthusia.model.EnthusiaCommittee;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class EnthusiaDepartmentHeadsDialog extends Dialog {

    private Activity activity;

    public EnthusiaDepartmentHeadsDialog(Activity activity) {
        super((activity));
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enthusia_dialog_deaprtment_heads);

        ArrayList<EnthusiaCommittee> mItems = new ArrayList<EnthusiaCommittee>();


        // TODO Add Department Heads
        mItems.add(new EnthusiaCommittee("A: +91", 0));
        mItems.add(new EnthusiaCommittee("B: +91", 0));
        mItems.add(new EnthusiaCommittee("C: +91", 1));
        mItems.add(new EnthusiaCommittee("D: +91", 1));

        ((StickyListHeadersListView) findViewById(R.id.enthusia_dialog_department_heads_list)).setAdapter(new EnthusiaStickyHeaderAdapter(activity, mItems, getContext().getResources().getStringArray(R.array.enthusia_departments)));
    }
}
