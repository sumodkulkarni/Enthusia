package org.enthusia.app.enthusia.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.enthusia.adapters.EnthusiaStickyHeaderAdapter;
import org.enthusia.app.enthusia.model.EnthusiaCommittee;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class EnthusiaDepartmentHeadsFragment extends Fragment {

    private StickyListHeadersListView listView;
    private ArrayList<EnthusiaCommittee> mItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.enthusia_dialog_deaprtment_heads, container, false);
        listView = (StickyListHeadersListView) v.findViewById(R.id.enthusia_dialog_department_heads_list);
        return v;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            animateAppear(true);
            ((EnthusiaStartActivity) getActivity()).getSupportActionBar().setTitle("Intra");
            ((EnthusiaStartActivity) getActivity()).currentFragment = new EnthusiaIntraFragment();
            ((EnthusiaStartActivity) getActivity()).lockDrawer(false);
        } catch (Exception ignore) {}
    }

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup ActionBar
        try {
            animateAppear(false);
            ((EnthusiaStartActivity) getActivity()).getSupportActionBar().setTitle("Department Heads");
            ((EnthusiaStartActivity) getActivity()).title.add("Department Heads");
            ((EnthusiaStartActivity) getActivity()).currentFragment = this;
            ((EnthusiaStartActivity) getActivity()).lockDrawer(true);
        } catch (Exception ignore) {}

        if (savedInstanceState == null)
            mItems = new ArrayList<>();
        else
            mItems = (ArrayList<EnthusiaCommittee>) savedInstanceState.getSerializable("items");

        // Chemsa
        mItems.add(new EnthusiaCommittee("Akash Gurle: +917045870817", 0));
        mItems.add(new EnthusiaCommittee("Mandavi Tripathi: +918080848183", 0));

        // Civil
        mItems.add(new EnthusiaCommittee("Apeksha Khandelwal: +918237448486", 1));
        mItems.add(new EnthusiaCommittee("Vidit Hirani: +919769193997", 1));

        // Comps
        mItems.add(new EnthusiaCommittee("Komal Deoda: +9699229666", 2));
        mItems.add(new EnthusiaCommittee("Raj Chandak: +919664858392", 2));
        mItems.add(new EnthusiaCommittee("Sagar Hinduja: +919619287347", 2));
        mItems.add(new EnthusiaCommittee("Nikhita Mirchandani: +919004409222", 2));
        mItems.add(new EnthusiaCommittee("Sanika Raut: +919920250229", 2));
        mItems.add(new EnthusiaCommittee("Tejas Hegde: +919619486295", 2));

        // Electrical
        mItems.add(new EnthusiaCommittee(" : ", 3));
        mItems.add(new EnthusiaCommittee(" : ", 3));

        // Electronics
        mItems.add(new EnthusiaCommittee(" : ", 4));
        mItems.add(new EnthusiaCommittee(" : ", 4));

        // EXTC
        mItems.add(new EnthusiaCommittee("Ketaki Kothawade: +919870851092", 5));
        mItems.add(new EnthusiaCommittee("Joel D'Souza: +919920241397", 5));

        // IT
        mItems.add(new EnthusiaCommittee("Shubham Shah: +918625840845", 6));
        mItems.add(new EnthusiaCommittee("Pratiksha Dehade: +919699886355", 6));

        // Masters
        mItems.add(new EnthusiaCommittee(" : ", 7));
        mItems.add(new EnthusiaCommittee(" : ", 7));

        // Mechanical
        mItems.add(new EnthusiaCommittee("Swaroop Atnoorkar: +918108282968", 8));
        mItems.add(new EnthusiaCommittee("Akshay Korpe: +918767077344", 8));

        // Production
        mItems.add(new EnthusiaCommittee("Ayesha Passanha: +919167130184", 9));
        mItems.add(new EnthusiaCommittee("Mayur Marakwad: +919403211077", 9));

        // Textile
        mItems.add(new EnthusiaCommittee("Yogesh Pantoji: +918793152342", 10));
        mItems.add(new EnthusiaCommittee(" : ", 10));

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(new EnthusiaStickyHeaderAdapter(getActivity(), mItems, getActivity().getResources().getStringArray(R.array.enthusia_departments)));
        StickyListHeadersAdapterDecorator decorator = new StickyListHeadersAdapterDecorator(animationAdapter);
        decorator.setStickyListHeadersListView(listView);
        listView.setAdapter(decorator);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("items", mItems);
    }

    Drawable hamburger;

    @SuppressWarnings("ConstantConditions")
    public void animateAppear(final boolean destroying) {

        if (destroying) {
            ((Toolbar) getActivity().findViewById(R.id.action_bar)).setNavigationIcon(hamburger);
        } else {
            if (hamburger == null)
                hamburger = ((Toolbar) getActivity().findViewById(R.id.action_bar)).getNavigationIcon();
            TypedValue value = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.homeAsUpIndicator, value, true);
            ((Toolbar) (getActivity()).findViewById(R.id.action_bar)).setNavigationIcon(value.resourceId);
        }

    }

}
