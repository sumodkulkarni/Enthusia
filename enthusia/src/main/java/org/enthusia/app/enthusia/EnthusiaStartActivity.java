package org.enthusia.app.enthusia;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.fragments.EnthusiaAboutFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaEventsFragment;
import org.enthusia.app.gcm.RegisterActivity;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaNavDrawerAdapter;
import org.enthusia.app.enthusia.fragments.EnthusiaCommitteeFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaIntraFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaNewsFragment;
import org.enthusia.app.enthusia.fragments.EnthusiaSponsorsFragment;
import org.enthusia.app.enthusia.model.EnthusiaNavDrawerItem;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class EnthusiaStartActivity extends Activity {

    private final static int[] SOCIAL_MEDIA_DRAWABLES = {
            R.drawable.ic_fab_facebook,
            R.drawable.ic_fab_twitter,
            R.drawable.ic_fab_youtube,
            R.drawable.ic_fab_website
    };

    private DrawerLayout enthusiaSlider;
    private ActionBarDrawerToggle enthusiaToggle;
    private Fragment currentFragment;

    private ArrayList<FloatingActionButton> socialMediaIcons;
    private boolean socialMediaShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_start);

        enthusiaSlider = (DrawerLayout) findViewById(R.id.enthusia_start_drawer);
        enthusiaSlider.setFocusableInTouchMode(false);
        ArrayList<EnthusiaNavDrawerItem> mItems = new ArrayList<EnthusiaNavDrawerItem>();

        for (int i=0; i < getResources().getStringArray(R.array.enthusia_nav_drawer_items).length; i++) {
            mItems.add(new EnthusiaNavDrawerItem(getResources().getStringArray(R.array.enthusia_nav_drawer_items)[i], getResources().obtainTypedArray(R.array.enthusia_nav_drawer_icons).getResourceId(i, -1)));
        }

        ((ListView) findViewById(R.id.enthusia_start_slider)).setAdapter(new EnthusiaNavDrawerAdapter(this, mItems));
        ((ListView) findViewById(R.id.enthusia_start_slider)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                displayView(position);
            }
        });

        ((TextView) findViewById(R.id.enthusia_start_user)).setText(getString(R.string.welcome) + ", " + Utils.getPrefs(getApplicationContext(), Utils.PREF_USER_NAME, String.class));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setCustomView(R.layout.actionbar_custom);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().getCustomView().findViewById(R.id.actionbar_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
                    enthusiaSlider.openDrawer(Gravity.LEFT);
                } else {
                    enthusiaSlider.closeDrawers();
                }
            }
        });
        /*getActionBar().getCustomView().findViewById(R.id.actionbar_title_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
                    enthusiaSlider.openDrawer(Gravity.LEFT);
                } else {
                    enthusiaSlider.closeDrawers();
                }
            }
        });
        getActionBar().getCustomView().findViewById(R.id.actionbar_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
                    enthusiaSlider.openDrawer(Gravity.LEFT);
                } else {
                    enthusiaSlider.closeDrawers();
                }
            }
        });*/

        enthusiaToggle = new ActionBarDrawerToggle(this, enthusiaSlider, android.R.color.transparent, R.string.enthusia_fest_name, R.string.enthusia_events) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        enthusiaSlider.setDrawerListener(enthusiaToggle);

        setupSocialMedia();

        if (getIntent().getExtras() != null && getIntent().hasExtra("points")) {
            if (getIntent().getExtras().getBoolean("points"))
                displayView(2);
            else
                displayView(0);
        } else if (savedInstanceState == null) {
            displayView(0);
        } else {
            displayView(savedInstanceState.getInt("fragment"));
        }

        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.enthusia_start_slider));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                enthusiaToggle.onOptionsItemSelected(item);
                break;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return enthusiaToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enthusiaToggle.syncState();

        if (!((Boolean) Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_REGISTRATION_DONE, Boolean.class))) {
            startActivityForResult(new Intent(EnthusiaStartActivity.this, RegisterActivity.class), 47);
        } else {
            if (savedInstanceState == null)
                Utils.showInfo(EnthusiaStartActivity.this, "Welcome back, " + Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_USER_NAME, String.class));
            if (!((Boolean) Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_FIRST_RUN, Boolean.class))) {
                help();
            }
        }
     }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 47) {
            if (resultCode == RESULT_OK) {
                ( (TextView) findViewById(R.id.enthusia_start_user)).setText("Welcome, " + Utils.getPrefs(this, Utils.PREF_USER_NAME, String.class));
                help();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        enthusiaToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
            finish();
        } else if (currentFragment != null && currentFragment instanceof EnthusiaEventsFragment) {
            EnthusiaEventsFragment fragment = (EnthusiaEventsFragment) currentFragment;
            if (fragment.mUnfoldableView != null && (fragment.mUnfoldableView.isUnfolded() || fragment.mUnfoldableView.isUnfolding())) {
                fragment.reset();
            } else {
                enthusiaSlider.openDrawer(Gravity.LEFT);
            }
        } else {
            enthusiaSlider.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    protected void onDestroy() {
        enthusiaSlider.closeDrawers();
        super.onDestroy();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (!enthusiaSlider.isDrawerOpen(GravityCompat.START)) {
            enthusiaSlider.openDrawer(Gravity.LEFT);
        } else {
            enthusiaSlider.closeDrawers();
        }
        return true;
    }


    private void setupSocialMedia() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enthusia_start_fab);
        fab.setColor(getResources().getColor(R.color.white));
        fab.setDrawable(getResources().getDrawable(R.drawable.ic_fab_launcher));
        fab.setOnClickListener(fabClick);

        socialMediaIcons = new ArrayList<FloatingActionButton>();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( (int) (72 *  getResources().getDisplayMetrics().scaledDensity), (int) (72 * getResources().getDisplayMetrics().scaledDensity));
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.bottomMargin = (int) (16 * getResources().getDisplayMetrics().scaledDensity);
        params.rightMargin = (int) (16 * getResources().getDisplayMetrics().scaledDensity);

        for (int i=0; i < SOCIAL_MEDIA_DRAWABLES.length; i++) {
            FloatingActionButton button = new FloatingActionButton(EnthusiaStartActivity.this);
            button.setDrawable(getResources().getDrawable(SOCIAL_MEDIA_DRAWABLES[i]));
            button.setColor(getResources().getColor(R.color.white));
            button.setVisibility(View.GONE);
            button.setId(i);
            button.setLayoutParams(params);
            button.setOnClickListener(fabItemClick);

            ((FrameLayout) findViewById(R.id.enthusia_start_fab_container)).addView(button);
            socialMediaIcons.add(button);
        }

    }

    private View.OnClickListener fabClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            reveal();
        }
    };

    private void reveal() {
        AnimatorSet reveal = new AnimatorSet();
        reveal.setDuration(500);
        reveal.setInterpolator(new OvershootInterpolator(1.0f));
        reveal.addListener(getListener());
        for (int i=0; i < socialMediaIcons.size(); i++)
            reveal.play(getAnimation(socialMediaIcons.get(i), i+1));
        reveal.play(getFABAnimation());
        reveal.start();
    }

    private Animator getFABAnimation() {
        AnimatorSet set = new AnimatorSet();
        set.play(getFABAnimationPhase1()).before(getFABAnimationPhase2());
        return set;
    }

    private Animator getFABAnimationPhase1() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.enthusia_start_fab), View.ROTATION_Y, 0, 90);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.enthusia_start_fab).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (socialMediaShown)
                    ((FloatingActionButton) findViewById(R.id.enthusia_start_fab)).setDrawable(getResources().getDrawable(R.drawable.ic_fab_launcher));
                else
                    ((FloatingActionButton) findViewById(R.id.enthusia_start_fab)).setDrawable(getResources().getDrawable(R.drawable.ic_close));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private Animator getFABAnimationPhase2() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.enthusia_start_fab), View.ROTATION_Y, 90, 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                findViewById(R.id.enthusia_start_fab).setOnClickListener(fabClick);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private Animator getAnimation(FloatingActionButton button, int i) {
        AnimatorSet set = new AnimatorSet();
        if (socialMediaShown) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                set.play(ObjectAnimator.ofFloat(button, View.Y, findViewById(R.id.enthusia_start_fab).getY()));
            else
                set.play(ObjectAnimator.ofFloat(button, View.X, findViewById(R.id.enthusia_start_fab).getX()));
            set.play(ObjectAnimator.ofFloat(button, View.ROTATION, 0, 360.0f));
            set.play(ObjectAnimator.ofFloat(button, View.ALPHA, 1.0f, 0.0f));

        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                set.play(ObjectAnimator.ofFloat(button, View.TRANSLATION_Y, -button.getLayoutParams().height * i));
            else
                set.play(ObjectAnimator.ofFloat(button, View.TRANSLATION_X, -button.getLayoutParams().height * i));
            set.play(ObjectAnimator.ofFloat(button, View.ROTATION, 0, 360.0f));
            set.play(ObjectAnimator.ofFloat(button, View.ALPHA, 0.0f, 1.0f));
        }
        return set;

    }

    private Animator.AnimatorListener getListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (FloatingActionButton button : socialMediaIcons) {
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(null);
                }

                findViewById(R.id.enthusia_start_fab).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (FloatingActionButton button : socialMediaIcons)
                    button.setOnClickListener(fabItemClick);

                if (socialMediaShown) {
                    for (FloatingActionButton button : socialMediaIcons)
                        button.setVisibility(View.GONE);
                }

                socialMediaShown = !socialMediaShown;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private View.OnClickListener fabItemClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case 0:
                    try {
                        getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/158407949426"));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/enthusia.vjti"));
                    }
                    break;

                case 1:
                    try {
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=192904564"));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ENTHUSIAVJTI"));
                    }
                    break;

                case 2:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/channel/UCKhaVXEDjLjt5YAqhL19yLA?feature=watch"));
                    break;

                case 3:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.enthusia.org/"));
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        }
    };

    private void displayView (int position) {
        currentFragment = null;
        switch (position) {
            case 0:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((getString(R.string.enthusia_fest_name)));
                currentFragment = new EnthusiaNewsFragment();
                break;
            case 1:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((R.string.enthusia_events));
                currentFragment = new EnthusiaEventsFragment();
                break;
            case 2:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((getString(R.string.enthusia_intra)));
                currentFragment = new EnthusiaIntraFragment();
                break;
            case 3:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((getString(R.string.enthusia_sponsors)));
                currentFragment = new EnthusiaSponsorsFragment();
                break;
            case 4:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((getString(R.string.enthusia_committee)));
                currentFragment = new EnthusiaCommitteeFragment();
                break;
            case 5:
                ((TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_title_text)).setText((getString(R.string.enthusia_about)));
                currentFragment = new EnthusiaAboutFragment();
                break;
        }

        setSelected(position);
        enthusiaSlider.closeDrawers();

        if (currentFragment != null) {
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.enthusia_start_fragment_container, currentFragment)
                                .commit();
                    }
                }, 240);
            } catch (Exception ignore) {}
        }
    }

    private void setSelected(int id) {
        for (int i=0; i < ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getCount(); i++) {
            ( (EnthusiaNavDrawerItem) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getItem(i)).setSelected(id == i);
        }
        ( (EnthusiaNavDrawerAdapter) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter()).notifyDataSetChanged();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragment", getFragment());
    }

    private int getFragment() {
        if (currentFragment != null) {
            if (currentFragment instanceof EnthusiaNewsFragment)
                return 0;
            else if (currentFragment instanceof EnthusiaEventsFragment)
                return 1;
            else if (currentFragment instanceof EnthusiaIntraFragment)
                return 2;
            else if (currentFragment instanceof EnthusiaSponsorsFragment)
                return 3;
            else if (currentFragment instanceof EnthusiaCommitteeFragment)
                return 4;
            else if (currentFragment instanceof EnthusiaAboutFragment)
                return 5;
        }
        return 0;
    }

    private void help() {
       findViewById(R.id.enthusia_start_fab).performClick();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.enthusia_start_fab).performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enthusiaSlider.openDrawer(Gravity.LEFT);
                        Utils.putPrefs(getApplicationContext(), Utils.PREF_FIRST_RUN, true);
                    }
                }, 250);
            }
        }, 2000);
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