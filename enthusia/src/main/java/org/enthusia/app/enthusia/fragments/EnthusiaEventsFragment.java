package org.enthusia.app.enthusia.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;

import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsEventHeadAdapter;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsGridAdapter;
import org.enthusia.app.enthusia.model.EnthusiaEvents;
import org.enthusia.app.ui.MaterialRippleLayout;

public class EnthusiaEventsFragment extends Fragment implements View.OnClickListener {

    public UnfoldableView mUnfoldableView;
    public boolean isExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_events, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isExpanded", isExpanded);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null)
            isExpanded = savedInstanceState.getBoolean("isExpanded");

        showDetailsView(false);

        mUnfoldableView = (UnfoldableView) getActivity().findViewById(R.id.enthusia_events_unfoldable_view);
        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {

            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                showDetailsView(true);
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                ((ImageButton) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_home_as_up));
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {}

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                showDetailsView(false);
                ((TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText(getString(R.string.enthusia_events));
                ((ImageButton) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_icon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_cab_drawer));
            }
        });

        ((StaggeredGridView) getActivity().findViewById(R.id.enthusia_events_grid)).setAdapter(new EnthusiaEventsGridAdapter(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long id) {
                setupEvent(view.findViewById(R.id.enthusia_events_list_item_event_image),
                        position);
            }
        }));
        ((StaggeredGridView) getActivity().findViewById(R.id.enthusia_events_grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MaterialRippleLayout) view).getChildView().performClick();
            }
        });

        getActivity().findViewById(R.id.enthusia_events_touch_interceptor_view).setClickable(false);
    }

    /**
     * UnFlodable View Events
     */

    private void showDetailsView (boolean show) {
        getActivity().findViewById(R.id.enthusia_events_details_layout).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupEvent (View view, final int event) {
        ((TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText(EnthusiaEvents.events[event]);

        // Event Image
        DynamicHeightImageView imageView = (DynamicHeightImageView) getActivity().findViewById(R.id.enthusia_events_details_event_image);
        imageView.setHeightRatio(1.0);
        imageView.setImageResource(EnthusiaEvents.drawables[event]);

        // Event Heads
        ((ListView) getActivity().findViewById(R.id.enthusia_events_details_event_list_event_heads)).setAdapter(new EnthusiaEventsEventHeadAdapter(getActivity(), EnthusiaEvents.getEventHead(event)));
        setListViewHeightBasedOnChildren((ListView) getActivity().findViewById(R.id.enthusia_events_details_event_list_event_heads));

        // Rules
        ((TextView) getActivity().findViewById(R.id.enthusia_event_rules)).setText(Html.fromHtml(getString(EnthusiaEvents.rules[event])));

        // Expand Listener
        getActivity().findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(this);
        getActivity().findViewById(R.id.enthusia_event_showmore).setOnClickListener(this);

        mUnfoldableView.unfold(view, getActivity().findViewById(R.id.enthusia_events_details_layout));
    }

    public void reset() {
        if (isExpanded)
            getActivity().findViewById(R.id.enthusia_event_showmore).performClick();
        ((ScrollView) getActivity().findViewById(R.id.enthusia_events_details_layout)).fullScroll(View.FOCUS_UP);
        mUnfoldableView.foldBack();
        isExpanded = false;
    }

    @Override
    public void onClick(View v) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(getTextViewAnimation()).with(getImageViewAnimation());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                getActivity().findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExpanded = !isExpanded;
                getActivity().findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(EnthusiaEventsFragment.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    private ObjectAnimator getTextViewAnimation() {
        ObjectAnimator animator =  ObjectAnimator.ofFloat(getActivity().findViewById(R.id.enthusia_event_rules),
                "maxLines",
                3, 30);

        if (isExpanded) {
            animator.setFloatValues(30, 3);
        }

        return animator;
    }

    private ObjectAnimator getImageViewAnimation() {
        return ObjectAnimator.ofFloat(getActivity().findViewById(R.id.enthusia_event_showmore_image),
                View.ROTATION,
                getActivity().findViewById(R.id.enthusia_event_showmore_image).getRotation(),
                getActivity().findViewById(R.id.enthusia_event_showmore_image).getRotation() + 180);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
