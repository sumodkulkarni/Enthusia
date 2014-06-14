package org.enthusia.app.enthusia;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import org.enthusia.app.R;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsEventHeadAdapter;
import org.enthusia.app.enthusia.adapters.EnthusiaEventsGridAdapter;
import org.enthusia.app.enthusia.model.EnthusiaEvents;

public class EnthusiaEventsActivity extends Activity {

    private UnfoldableView mUnfoldableView;

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

        ((GridView)findViewById(R.id.enthusia_events_grid)).setAdapter(new EnthusiaEventsGridAdapter(this));
        ((GridView)findViewById(R.id.enthusia_events_grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        findViewById(R.id.enthusia_events_details_event_image).setBackgroundResource(EnthusiaEvents.drawables[event]);

        // Event Heads
        ((ListView) findViewById(R.id.enthusia_events_details_event_list_event_heads)).setAdapter(new EnthusiaEventsEventHeadAdapter(this, EnthusiaEvents.getEventHead(event)));

        // Rules
        findViewById(R.id.enthusia_events_details_event_button_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(EnthusiaEventsActivity.this)
                                                    .setCancelable(true)
                                                    .setTitle(getString(R.string.enthusia_rules))
                                                    .setMessage(Html.fromHtml(getString(EnthusiaEvents.rules[event])).toString())
                                                    .setPositiveButton(getString(android.R.string.yes), null)
                                                    .create();
                dialog.setCanceledOnTouchOutside(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
        });

        mUnfoldableView.unfold(view, findViewById(R.id.enthusia_events_details_layout));
    }

}
