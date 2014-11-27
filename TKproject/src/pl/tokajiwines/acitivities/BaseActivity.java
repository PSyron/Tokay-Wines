
package pl.tokajiwines.acitivities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import pl.tokajiwines.R;
import pl.tokajiwines.asyncs.GetSearchItemsAsync;
import pl.tokajiwines.utils.SuggestionProvider;

public class BaseActivity extends Activity {
    
    public GetSearchItemsAsync mLoadSearchItemsTask;

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_with_home, menu);
        
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         SearchView searchView =
                 (SearchView) menu.findItem(R.id.action_search).getActionView();
         searchView.setSearchableInfo(
                 searchManager.getSearchableInfo(getComponentName()));
         
        restoreActionBar();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    protected void onPause() {

        if (mLoadSearchItemsTask != null) {

            mLoadSearchItemsTask.cancel(true);            
            mLoadSearchItemsTask = null;
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {

            case android.R.id.home: {

                onBackPressed();
                return true;
            }

            case R.id.action_settings: {
                return true;
            }
            case R.id.action_home: {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                return true;

            }
            case R.id.action_search: {
                if (SuggestionProvider.sSearchItems == null)
                {
                    mLoadSearchItemsTask = new GetSearchItemsAsync(BaseActivity.this);
                    mLoadSearchItemsTask.execute();
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
