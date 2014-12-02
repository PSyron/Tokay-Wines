
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Restaurant;

import java.io.Serializable;

public class RestaurantDetails implements Serializable {

    /**
     * 
     */

    /**
     * 
     */

    public RestaurantDetails(int idRestaurant, String email, String link, String name,
            String phone, String vast, String streetName, String streetNumber, String houseNumber,
            String city, String lat, String lng, String postCode, String lastUpdate) {
        super();
        mIdRestaurant = idRestaurant;
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

    public RestaurantDetails(Restaurant r) {
        mIdRestaurant = r.mIdRestaurant;
        mEmail = r.mEmail;
        mLink = r.mLink;
        mName = r.mName;
        mPhone = r.mPhone;
        mVast = r.description.mVast;
        mStreetName = r.address.mStreetName;
        mStreetNumber = r.address.mStreetNumber + "";
        mHouseNumber = r.address.mHouseNumber + "";
        mCity = r.address.mCity;
        mLat = r.address.mLatitude + "";
        mLng = r.address.mLongitude + "";
        mPostCode = r.address.mPostcode;
        mLastUpdate = r.mLastUpdate;
        mImageUrl = r.imageCover.mImage;
    }

    private static final long serialVersionUID = -8116924641773895022L;
    @SerializedName("message")
    public String mMessage;
    @SerializedName("success")
    public String mSuccess;
    @SerializedName("idRestaurant")
    public int mIdRestaurant;
    @SerializedName("name")
    public String mName;
    @SerializedName("phone")
    public String mPhone;
    @SerializedName("image")
    public String mImageUrl;
    @SerializedName("streetName")
    public String mStreetName;
    @SerializedName("streetNumber")
    public String mStreetNumber;
    @SerializedName("houseNumber")
    public String mHouseNumber;
    @SerializedName("city")
    public String mCity;
    @SerializedName("postCode")
    public String mPostCode;
    @SerializedName("vast")
    public String mVast;
    @SerializedName("email")
    public String mEmail;
    @SerializedName("link")
    public String mLink;
    @SerializedName("latitude")
    public String mLat;
    @SerializedName("longitude")
    public String mLng;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

}
