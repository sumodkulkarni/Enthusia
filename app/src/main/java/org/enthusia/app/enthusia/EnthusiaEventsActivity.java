package org.enthusia.app.enthusia;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.etsy.android.grid.StaggeredGridView;
import com.neopixl.pixlui.components.textview.TextView;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsEventHeadAdapter;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsGridAdapter;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

public class EnthusiaEventsActivity extends Activity implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private UnfoldableView mUnfoldableView;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_events);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ex) {}

        showDetailsView(false);

        mUnfoldableView = (UnfoldableView) findViewById(R.id.enthusia_events_unfoldable_view);
        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {

            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                showDetailsView(true);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                findViewById(R.id.enthusia_events_touch_interceptor_view).setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                findViewById(R.id.enthusia_events_touch_interceptor_view).setClickable(false);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                showDetailsView(false);
                getActionBar().setTitle(getString(R.string.enthusia_events));
            }
        });

        ((StaggeredGridView)findViewById(R.id.enthusia_events_grid)).setAdapter(new EnthusiaEventsGridAdapter());
        ((StaggeredGridView)findViewById(R.id.enthusia_events_grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setupEvent(view.findViewById(R.id.enthusia_events_list_item_event_image),
                        position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                findViewById(R.id.enthusia_events_details_layout).getViewTreeObserver().removeGlobalOnLayoutListener(this);
            else
                findViewById(R.id.enthusia_events_details_layout).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mUnfoldableView.foldBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * UnFlodable View Events
     */

    private void showDetailsView (boolean show) {
        findViewById(R.id.enthusia_events_touch_interceptor_view).setClickable(show);
        findViewById(R.id.enthusia_events_details_layout).setVisibility(show == true ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupEvent (View view, final int event) {
        getActionBar().setTitle(EnthusiaEvents.events[event]);

        // Set ScrollView

        findViewById(R.id.enthusia_events_details_layout).getViewTreeObserver().addOnGlobalLayoutListener(this);

        findViewById(R.id.enthusia_events_details_event_image).setBackgroundResource(EnthusiaEvents.drawables[event]);

        // Event Heads
        ((ListView) findViewById(R.id.enthusia_events_details_event_list_event_heads)).setAdapter(new EnthusiaEventsEventHeadAdapter(this, EnthusiaEvents.getEventHead(event)));
        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.enthusia_events_details_event_list_event_heads));

        // Rules
        ((TextView) findViewById(R.id.enthusia_event_rules)).setText(Html.fromHtml(getString(EnthusiaEvents.rules[event])));

        // Expand Listener
        findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(this);

        mUnfoldableView.unfold(view, findViewById(R.id.enthusia_events_details_layout));
    }

    @Override
    public void onGlobalLayout() {
        findViewById(R.id.enthusia_events_details_layout).post(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.enthusia_events_details_layout)).fullScroll(View.FOCUS_DOWN);
            }
        });
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
                findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExpanded = !isExpanded;
                findViewById(R.id.enthusia_event_showmore_image).setOnClickListener(EnthusiaEventsActivity.this);
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
        ObjectAnimator animator =  ObjectAnimator.ofFloat(findViewById(R.id.enthusia_event_rules),
                "maxLines",
                3, 30);

        if (isExpanded) {
            animator.setFloatValues(30, 3);
        }

        return animator;
    }

    private ObjectAnimator getImageViewAnimation() {
        return ObjectAnimator.ofFloat(findViewById(R.id.enthusia_event_showmore_image),
                View.ROTATION,
                findViewById(R.id.enthusia_event_showmore_image).getRotation(),
                findViewById(R.id.enthusia_event_showmore_image).getRotation() + 180);
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
