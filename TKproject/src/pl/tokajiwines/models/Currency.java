
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Currency implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7762966093151493997L;
    @SerializedName("IdCurrency")
    public int mIdCurrency;
    @SerializedName("Name")
    public String mName;
    @SerializedName("NameShort")
    public String mNameShort;
    @SerializedName("Ratio")
    public float mRatio;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Currency(int IdCurrency, String Name, String NameShort, float Ratio, String LastUpdate) {
        super();
        mIdCurrency = IdCurrency;
        mName = Name;
        mNameShort = NameShort;
        mRatio = Ratio;
        mLastUpdate = LastUpdate;
    }

    public Currency() {
        // TODO Auto-generated constructor stub
    }
}
