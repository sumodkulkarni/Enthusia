package org.enthusia.app.enthusia.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
            ((TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText("Intra");
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
            ((TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText("Department Heads");
            ((EnthusiaStartActivity) getActivity()).title.add("Department Heads");
            ((EnthusiaStartActivity) getActivity()).currentFragment = this;
            ((EnthusiaStartActivity) getActivity()).lockDrawer(true);
        } catch (Exception ignore) {}

        if (savedInstanceState == null)
            mItems = new ArrayList<EnthusiaCommittee>();
        else
            mItems = (ArrayList<EnthusiaCommittee>) savedInstanceState.getSerializable("items");

        // Chemsa
        mItems.add(new EnthusiaCommittee("Kshiteej Nanaware: +918767485808", 0));
        mItems.add(new EnthusiaCommittee("Ashwini Bhosle: +919029391087", 0));

        // Civil
        mItems.add(new EnthusiaCommittee("Viraj Sanghvi: +919819584725", 1));
        mItems.add(new EnthusiaCommittee("Akshata Hire: +917208307426", 1));

        // Comps
        mItems.add(new EnthusiaCommittee("Siddhant Shah: +919930851934", 2));
        mItems.add(new EnthusiaCommittee("Richa Deshmukh: +919930539242", 2));

        // Electrical
        mItems.add(new EnthusiaCommittee("Arvind Nair: +919167112743", 3));
        mItems.add(new EnthusiaCommittee("Pradnya Shivsharan: +918976442036", 3));

        // Electronics
        mItems.add(new EnthusiaCommittee("Amit Lokare: +917350940343", 4));
        mItems.add(new EnthusiaCommittee("Prashita Pratapan: +919757341789", 4));

        // EXTC
        mItems.add(new EnthusiaCommittee("Dhruv Turkhia: +919664016227", 5));
        mItems.add(new EnthusiaCommittee("Aishwarya Shejwal: +919167186391", 5));

        // IT
        mItems.add(new EnthusiaCommittee("Sagar Sable: +919766979737", 6));
        mItems.add(new EnthusiaCommittee("Priya Masne: +918806209812", 6));

        // Masters
        mItems.add(new EnthusiaCommittee("Ankit Wankhede: +919561418177", 7));
        mItems.add(new EnthusiaCommittee("Swapnali Vadar: +919372672000", 7));

        // Mechanical
        mItems.add(new EnthusiaCommittee("Sayyad Sirhan: +919730228606", 8));
        mItems.add(new EnthusiaCommittee("Sakina Tinwala: +919619331995", 8));

        // Production
        mItems.add(new EnthusiaCommittee("Shivraj Pawar: +919699902679", 9));
        mItems.add(new EnthusiaCommittee("Rutuja Karampure: +919769951513", 9));

        // Textile
        mItems.add(new EnthusiaCommittee("Pakshal Jain: +919764040991", 10));
        mItems.add(new EnthusiaCommittee("Swati Dighole: +919768350702", 10));
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

    @SuppressWarnings("ConstantConditions")
    public void animateAppear(final boolean destroying) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon), View.ROTATION, 0, 360);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    if ((Float) animation.getAnimatedValue(View.ROTATION.getName()) > 50.0f)
                        if (destroying)
                            ((ImageButton) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon)).setImageResource(R.drawable.ic_cab_drawer);
                        else
                            ((ImageButton) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon)).setImageResource(R.drawable.ic_action_home_as_up);
                } catch (Exception ignore) {}
            }

        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                try {
                    getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon).setClickable(false);
                } catch (Exception ignore) {}
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon).setClickable(true);
                } catch (Exception ignore) {}
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

}
