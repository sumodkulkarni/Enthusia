package org.enthusia.app.enthusia;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import org.enthusia.app.enthusia.ui.android.view.ext.SatelliteMenu;
import org.enthusia.app.enthusia.ui.android.view.ext.SatelliteMenuItem;
import org.enthusia.app.ui.ActivitySplitAnimationUtil;
import org.enthusia.app.ui.RoundedDrawable;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class EnthusiaStartActivity extends Activity {

    private DrawerLayout enthusiaSlider;
    private ActionBarDrawerToggle enthusiaToggle;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_start);

        try {
            ActivitySplitAnimationUtil.prepareAnimation(this);
            ActivitySplitAnimationUtil.animate(this, 1000);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

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

        enthusiaToggle = new ActionBarDrawerToggle(this, enthusiaSlider, R.drawable.ic_drawer, R.string.enthusia_fest_name, R.string.enthusia_fest_name) {
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
        }


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
            Utils.showInfo(EnthusiaStartActivity.this, "Welcome, " + Utils.getPrefs(EnthusiaStartActivity.this, Utils.PREF_USER_NAME, String.class));
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
                fragment.mUnfoldableView.foldBack();
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

    @Override
    protected void onStop() {
        ActivitySplitAnimationUtil.cancel();
        super.onStop();
    }

    private void setupSocialMedia() {

        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).setSatelliteDistance(170);
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).setTotalSpacingDegree(90);
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).setCloseItemsOnClick(true);
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).setExpandDuration(500);

        ArrayList<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(3, getScaledDrawable(R.drawable.website)));
        items.add(new SatelliteMenuItem(2, getScaledDrawable(R.drawable.youtube)));
        items.add(new SatelliteMenuItem(1, getScaledDrawable(R.drawable.twitter)));
        items.add(new SatelliteMenuItem(0, getScaledDrawable(R.drawable.facebook)));
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).addItems(items);
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            @Override
            public void eventOccured(int id) {
                Intent intent = null;
                switch (id) {
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
        });
    }

    private Drawable getScaledDrawable (int drawable) {
        return RoundedDrawable.fromBitmap(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap( ((BitmapDrawable) getResources().getDrawable(drawable)).getBitmap()
                , 64, 64, true)).getBitmap()).setCornerRadius(5.0f).setBorderColor(getResources().getColor(android.R.color.black));
    }

    private void displayView (int position) {
        currentFragment = null;
        switch (position) {
            case 0:
                getActionBar().setTitle(getString(R.string.enthusia_fest_name));
                currentFragment = new EnthusiaNewsFragment();
                break;
            case 1:
                getActionBar().setTitle(R.string.enthusia_events);
                currentFragment = new EnthusiaEventsFragment();
                break;
            case 2:
                getActionBar().setTitle(getString(R.string.enthusia_intra));
                currentFragment = new EnthusiaIntraFragment();
                break;
            case 3:
                getActionBar().setTitle(getString(R.string.enthusia_sponsors));
                currentFragment = new EnthusiaSponsorsFragment();
                break;
            case 4:
                getActionBar().setTitle(getString(R.string.enthusia_committee));
                currentFragment = new EnthusiaCommitteeFragment();
                break;
            case 5:
                getActionBar().setTitle(getString(R.string.enthusia_about));
                currentFragment = new EnthusiaAboutFragment();
                break;
        }

        if (currentFragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.enthusia_start_fragment_container, currentFragment)
                    .commit();
        }

        setSelected(position);
        enthusiaSlider.closeDrawers();
    }

    private void setSelected(int id) {
        for (int i=0; i < ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getCount(); i++) {
            ( (EnthusiaNavDrawerItem) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter().getItem(i)).setSelected(id == i);
        }
        ( (EnthusiaNavDrawerAdapter) ((ListView) findViewById(R.id.enthusia_start_slider)).getAdapter()).notifyDataSetChanged();
    }


    private void help() {
        ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).expand();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((SatelliteMenu) findViewById(R.id.enthusia_start_social_media)).close();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enthusiaSlider.openDrawer(Gravity.LEFT);
                        Utils.putPrefs(getApplicationContext(), Utils.PREF_FIRST_RUN, true);
                    }
                }, 600);
            }
        }, 2000);
    }
}
