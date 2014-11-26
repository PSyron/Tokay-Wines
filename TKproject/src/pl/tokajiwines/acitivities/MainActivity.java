
package pl.tokajiwines.acitivities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import pl.tokajiwines.App;
import pl.tokajiwines.R;
import pl.tokajiwines.fragments.GuideFragment;
import pl.tokajiwines.fragments.MapFragment;
import pl.tokajiwines.fragments.NavigationDrawerFragment;
import pl.tokajiwines.fragments.NewsFragment;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.fragments.SettingsFragment;
import pl.tokajiwines.fragments.WinesFilterFragment;
import pl.tokajiwines.utils.SharedPreferencesHelper;

import java.util.Locale;

public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    boolean doubleBackToExitPressedOnce = false;
    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        if (App.isOnline(this)) {
        //            startActivity(new Intent(MainActivity.this, StartWineImageActivity.class));
        //        }
        // new RepeatServiceNotificationReceiver().onReceive(this, null);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        if (App.debug_mode) {
            SharedPreferencesHelper.clearSharedPreferences(this);
        }
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        onNavigationDrawerItemSelected(1);
        // Moved to StartWineImageActivity
        //initLanguage();

    }
    
    
    public void initLanguage() {
        int language;
        if (Locale.getDefault().getDisplayLanguage().contains("polsk")
                || Locale.getDefault().getDisplayLanguage().contains("pl")) {
            language = SharedPreferencesHelper.getSharedPreferencesInt(this,
                    SettingsFragment.SharedKeyLanguage, 0);
            if (language == 1) {
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                this.getApplicationContext().getResources().updateConfiguration(config, null);
                //odswież napisy w menu
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        } else {
            language = SharedPreferencesHelper.getSharedPreferencesInt(this,
                    SettingsFragment.SharedKeyLanguage, 0);
            if (language == 0) {
                Locale locale = new Locale("pl");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                this.getApplicationContext().getResources().updateConfiguration(config, null);
                //odswież napisy w menu
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.exit_msg), Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        position -= 1;
        if (position == 0) {
            mTitle = getString(R.string.title_news);
            fragmentManager.beginTransaction().replace(R.id.container, NewsFragment.newInstance())
                    .commit();
        } else if (position == 1) {
            mTitle = getString(R.string.title_wines);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, WinesFilterFragment.newInstance(this)).commit();
        } else if (position == 2) {
            mTitle = getString(R.string.title_wineyards);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProducersFragment.newInstance()).commit();
        } else if (position == 3) {
            mTitle = getString(R.string.title_map);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MapFragment.newInstance(this)).commit();
        } else if (position == 4) {
            mTitle = getString(R.string.title_tour);
            fragmentManager.beginTransaction().replace(R.id.container, GuideFragment.newInstance())
                    .commit();
        } else {
            mTitle = getString(R.string.title_settings);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance(this)).commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_news);
                break;
            case 2:
                mTitle = getString(R.string.title_wines);
                break;
            case 3:
                mTitle = getString(R.string.title_wineyards);
                break;
            case 4:
                mTitle = getString(R.string.title_map);
                break;
            case 5:
                mTitle = getString(R.string.title_tour);
                break;
            case 6:
                mTitle = getString(R.string.title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
             SearchView searchView =
                     (SearchView) menu.findItem(R.id.action_search).getActionView();
             searchView.setSearchableInfo(
                     searchManager.getSearchableInfo(getComponentName()));
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } /*else if (id == R.id.action_search) {
            .makeText(MainActivity.this, getResources().getString(R.string.not_working),
                    Toast.LENGTH_LONG).show();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
