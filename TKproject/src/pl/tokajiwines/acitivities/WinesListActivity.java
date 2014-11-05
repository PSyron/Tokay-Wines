
package pl.tokajiwines.acitivities;

import android.os.Bundle;
import android.widget.ListView;

import pl.tokajiwines.R;
import pl.tokajiwines.adapters.WinesAdapter;

public class WinesListActivity extends BaseActivity {

    ListView mUiList;
    WinesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wines);
        mUiList = (ListView) findViewById(R.id.activity_wines_list);
        mAdapter = new WinesAdapter(this);
        mUiList.setAdapter(mAdapter);
    }

}
