
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Hotel;

import java.io.Serializable;

public class HotelDetails implements Serializable {

    /**
     * 
     */

    public HotelDetails(int idHotel, String email, String link, String name, String phone,
            String vast, String streetName, String streetNumber, String houseNumber, String city,
            String lat, String lng, String postCode, String lastUpdate) {
        super();
        mIdHotel = idHotel;
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

    public HotelDetails(Hotel h) {
        mIdHotel = h.mIdHotel;
        mEmail = h.mEmail;
        mLink = h.mLink;
        mName = h.mName;
        mPhone = h.mPhone;
        mVast = h.description.mVast;
        mStreetName = h.address.mStreetName;
        mStreetNumber = h.address.mStreetNumber + "";
        mHouseNumber = h.address.mHouseNumber + "";
        mCity = h.address.mCity;
        mLat = h.address.mLatitude + "";
        mLng = h.address.mLongitude + "";
        mPostCode = h.address.mPostcode;
        mLastUpdate = h.mLastUpdate;
        mImageUrl = h.imageCover.mImage;
    }

    private static final long serialVersionUID = -3885082397146719097L;
    @SerializedName("message")
    public String mMessage;
    @SerializedName("success")
    public String mSuccess;
    @SerializedName("idHotel")
    public int mIdHotel;
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
