package com.vjti.fests.enthusia;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vjti.fests.Utils;
import com.vjti.fests.enthusia.fragments.EnthusiaCommitteeFragment;
import com.vjti.fests.enthusia.fragments.EnthusiaIntraFragment;
import com.vjti.fests.enthusia.fragments.EnthusiaNewsFragment;
import com.vjti.fests.enthusia.fragments.EnthusiaSponsorsFragment;
import com.vjti.fests.enthusia.model.EnthusiaEvents;
import com.vjti.fests.enthusia.ui.EnthusiaNavDrawerAdapter;
import com.vjti.fests.enthusia.model.EnthusiaNavDrawerItem;
import com.vjti.fests.enthusia.ui.android.view.ext.SatelliteMenu;
import com.vjti.fests.enthusia.ui.android.view.ext.SatelliteMenuItem;
import com.vjti.fests.ui.ActivitySplitAnimationUtil;
import com.vjti.fests.R;
import com.vjti.fests.ui.RoundedDrawable;

import java.util.ArrayList;

public class EnthusiaStartActivity extends Activity {

    private DrawerLayout enthusiaSlider;
    private ActionBarDrawerToggle enthusiaToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_start);
        try {
            ActivitySplitAnimationUtil.prepareAnimation(this);
            ActivitySplitAnimationUtil.animate(this, 1000);
        } catch (NullPointerException ex) {}

        enthusiaSlider = (DrawerLayout) findViewById(R.id.enthusia_start_drawer);
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

        ((TextView) findViewById(R.id.enthusia_start_user)).setText(getString(R.string.welcome) + ", " + getSharedPreferences(Utils.SHARED_PREFS, MODE_PRIVATE).getString(Utils.PREF_USER_NAME, ""));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        enthusiaToggle = new ActionBarDrawerToggle(this, enthusiaSlider, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
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

        if (savedInstanceState == null)
            displayView(0);

        setupSocialMedia();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (enthusiaToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enthusiaToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        enthusiaToggle.onConfigurationChanged(newConfig);
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
        items = null;
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

                intent = null;
            }
        });
    }

    private Drawable getScaledDrawable (int drawable) {
        return RoundedDrawable.fromBitmap(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap( ((BitmapDrawable) getResources().getDrawable(drawable)).getBitmap()
                , 64, 64, true)).getBitmap()).setCornerRadius(5.0f).setBorderColor(getResources().getColor(android.R.color.black));
    }

    private void displayView (int position) {

        Intent intent = null;
        Fragment fragment = null;
        switch (position) {
            case 0:
                getActionBar().setTitle(getString(R.string.enthusia_fest_name));
                fragment = new EnthusiaNewsFragment();
                break;
            case 1:
                intent = new Intent(this, EnthusiaEventsActivity.class);
                break;
            case 2:
                getActionBar().setTitle(getString(R.string.enthusia_intra));
                fragment = new EnthusiaIntraFragment();
                break;
            case 3:
                getActionBar().setTitle(getString(R.string.enthusia_sponsors));
                fragment = new EnthusiaSponsorsFragment();
                break;
            case 4:
                getActionBar().setTitle(getString(R.string.enthusia_committee));
                fragment = new EnthusiaCommitteeFragment();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        } else if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.enthusia_start_fragment_container, fragment).commit();
        }

        fragment = null;
        ((ListView) findViewById(R.id.enthusia_start_slider)).setSelection(position);
        ((ListView) findViewById(R.id.enthusia_start_slider)).setItemChecked(position, true);
        enthusiaSlider.closeDrawers();
    }
}
