
package pl.tokajiwines.acitivities;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import pl.tokajiwines.R;

public class AboutAppActivity extends BaseActivity {

    TextView mUiVersion;
    String mVersionName;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        initView();

    }

    public void initView() {

        mContext = this;
        getActionBar().setTitle(getResources().getString(R.string.about_the_application));
        mUiVersion = (TextView) findViewById(R.id.activity_about_app_version_tV);
        try {
            mVersionName = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            mUiVersion.setText(getResources().getString(R.string.version_problem));
        }
        mUiVersion.setText(mVersionName);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

}
