
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HotelListItem implements Serializable {

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

    public HotelListItem(int idHotel, String name, String phone, String streetName,
            String imageUrl, String streetNumber, String houseNumber, String city, String postCode) {
        super();
        mIdHotel = idHotel;
        mName = name;
        mPhone = phone;
        mImageUrl = imageUrl;
        mStreetName = streetName;
        mStreetNumber = streetNumber;
        mHouseNumber = houseNumber;
        mCity = city;
        mPostCode = postCode;

    }
    
    public HotelListItem(int id, String name)
    {
        mIdHotel = id;
        mName = name;
    }
}
