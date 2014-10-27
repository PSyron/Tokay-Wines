
package pl.tokajiwines.acitivities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import pl.tokajiwines.R;
import pl.tokajiwines.fragments.ProducersFragment;
import pl.tokajiwines.models.ProducerListItem;
import pl.tokajiwines.utils.Log;

public class ProducerAcitvity extends BaseActivity {

    public static int REQUEST = 997;

    ProducerListItem mProducer;

    TextView mUiTitle;
    ImageView mUiImage;
    TextView mUiAddress;
    TextView mUiOpeningHours;
    TextView mUiPhoneNumber;
    TextView mUiUrl;
    ImageView mUiNear;
    ImageView mUiNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mProducer = (ProducerListItem) extras.getSerializable(ProducersFragment.PRODUCER_TAG);
        }
        Log.e(ProducerAcitvity.class.getName(), mProducer + " ");
        initView();
        fillView();

    }

    public void initView() {
        mUiTitle = (TextView) findViewById(R.id.activity_news_details_name);
        mUiImage = (ImageView) findViewById(R.id.activity_news_details_image);
        mUiAddress = (TextView) findViewById(R.id.activity_producer_details_adress);
        mUiOpeningHours = (TextView) findViewById(R.id.activity_producer_details_hours);
        mUiPhoneNumber = (TextView) findViewById(R.id.activity_producer_details_phone);
        mUiUrl = (TextView) findViewById(R.id.activity_producer_details_url);
        mUiNear = (ImageView) findViewById(R.id.activity_producer_navigate);
        mUiNavigate = (ImageView) findViewById(R.id.activity_producer_neighborhood);
    }

    public void fillView() {
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(mProducer.mName);
        mUiTitle.setText(mProducer.mName);
    }

}
