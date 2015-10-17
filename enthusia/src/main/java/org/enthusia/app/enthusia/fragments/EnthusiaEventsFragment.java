package org.enthusia.app.enthusia.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.enthusia.EventRegistrationActivity;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsEventHeadAdapter;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsGridAdapter;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

public class EnthusiaEventsFragment extends Fragment implements View.OnClickListener {

    public UnfoldableView mUnfoldableView;
    Button newButton;
    public boolean isExpanded = false;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_events, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isExpanded", isExpanded);
        outState.putBoolean("isUnfolded", mUnfoldableView.isUnfolded());
        outState.putInt("position", position);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //newButton = (Button) getActivity().findViewById(R.id.event_register_button);

        if (savedInstanceState != null)
            isExpanded = savedInstanceState.getBoolean("isExpanded");

        showDetailsView(false);

        mUnfoldableView = (UnfoldableView) getActivity().findViewById(R.id.enthusia_events_unfoldable_view);
        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {

            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                showDetailsView(true);
                animateAppear(false);
                ((EnthusiaStartActivity) getActivity()).lockDrawer(true);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {

            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                ((EnthusiaStartActivity) getActivity()).lockDrawer(false);
                animateAppear(true);
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                showDetailsView(false);
                ((EnthusiaStartActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.enthusia_events));
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
     * UnFoldable View Events
     */

    private void showDetailsView (boolean show) {
        try {
            getActivity().findViewById(R.id.enthusia_events_details_layout).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception ignore) {}
    }

    @SuppressWarnings("ConstantConditions")
    private void setupEvent (View view, final int event) {

        position = event;

        ((EnthusiaStartActivity) getActivity()).getSupportActionBar().setTitle(EnthusiaEvents.events[event]);

        // Event Image
        DynamicHeightImageView imageView = (DynamicHeightImageView) getActivity().findViewById(R.id.enthusia_events_details_event_image);
        imageView.setHeightRatio(1.0);
        imageView.setImageResource(EnthusiaEvents.drawables[event]);

        // Event Heads
        ((ListView) getActivity().findViewById(R.id.enthusia_events_details_event_list_event_heads)).setAdapter(new EnthusiaEventsEventHeadAdapter(getActivity(), EnthusiaEvents.getEventHead(event)));


        // Rules
        ((TextView) getActivity().findViewById(R.id.enthusia_event_rules)).setText(Html.fromHtml(getString(EnthusiaEvents.rules[event])));

        getActivity().findViewById(R.id.enthusia_event_showmore).setVisibility(getString(EnthusiaEvents.rules[event]).toLowerCase().startsWith("please contact") ? View.GONE : View.VISIBLE);

        //button
        getActivity().findViewById(R.id.event_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EventRegistrationActivity.class);
                i.putExtra("Event", event);
                startActivity(i);
            }
        });

        // Expand Listener
        getActivity().findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(this);
        getActivity().findViewById(R.id.enthusia_event_showmore).setOnClickListener(this);

        setListViewHeightBasedOnChildren((ListView) getActivity().findViewById(R.id.enthusia_events_details_event_list_event_heads));

        mUnfoldableView.unfold(view, getActivity().findViewById(R.id.enthusia_events_details_layout));
    }

    public void reset() {
        if (isExpanded && getActivity().findViewById(R.id.enthusia_event_showmore).getVisibility() == View.VISIBLE)
            getActivity().findViewById(R.id.enthusia_event_showmore).performClick();
        ((ScrollView) getActivity().findViewById(R.id.enthusia_events_details_layout)).fullScroll(View.FOCUS_UP);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUnfoldableView.foldBack();
                isExpanded = false;
            }
        }, getActivity().findViewById(R.id.enthusia_event_showmore).getVisibility() == View.VISIBLE ? (isExpanded ?  300 : 100) : 100);
    }

    @Override
    public void onClick(View v) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
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
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
        params.height += listView.getPaddingBottom() + listView.getPaddingTop();
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    Drawable oldDrawable;

    @SuppressWarnings("ConstantConditions")
    public void animateAppear(final boolean destroying) {
        if (destroying) {
            ((Toolbar) (getActivity()).findViewById(R.id.action_bar)).setNavigationIcon(oldDrawable);
        } else {
            if (oldDrawable == null)
                oldDrawable = ((Toolbar) (getActivity()).findViewById(R.id.action_bar)).getNavigationIcon();
            TypedValue value = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.homeAsUpIndicator, value, true);
            ((Toolbar) (getActivity()).findViewById(R.id.action_bar)).setNavigationIcon(value.resourceId);
        }
    }

}
