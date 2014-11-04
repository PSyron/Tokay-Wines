
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProducerDetails implements Serializable {

    public ProducerDetails(int idProducer, String email, String link, String name, String phone,
            String vast, String streetName, String streetNumber, String houseNumber, String city,
            String lat, String lng, String postCode, String lastUpdate) {
        super();
        mIdProducer = idProducer;
        mEmail = email;
        mLink = link;
        mName = name;
        mPhone = phone;
        mVast = vast;
        mStreetName = streetName;
        mStreetNumber = streetNumber;
        mHouseNumber = houseNumber;
        mCity = city;
        mLat = lat;
        mLng = lng;
        mPostCode = postCode;
        mLastUpdate = lastUpdate;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 6577206673206194819L;
    @SerializedName("message")
    public String mMessage;
    @SerializedName("success")
    public String mSuccess;
    @SerializedName("idProducer")
    public int mIdProducer;
    @SerializedName("email")
    public String mEmail;
    @SerializedName("link")
    public String mLink;
    @SerializedName("name")
    public String mName;
    @SerializedName("phone")
    public String mPhone;  
    @SerializedName("image")
    public String mImageUrl;

    @SerializedName("vast")
    public String mVast;
    @SerializedName("streetName")
    public String mStreetName;
    @SerializedName("streetNumber")
    public String mStreetNumber;
    @SerializedName("houseNumber")
    public String mHouseNumber;
    @SerializedName("city")
    public String mCity;
    @SerializedName("latitude")
    public String mLat;
    @SerializedName("longitude")
    public String mLng;
    @SerializedName("postCode")
    public String mPostCode;
    
    @SerializedName("wineImage")
    public String mWineImageUrl;
    @SerializedName("wineName")
    public String mWineName;

    @SerializedName("LastUpdate")
    public String mLastUpdate;
}
