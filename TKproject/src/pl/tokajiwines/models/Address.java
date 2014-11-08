
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6165102167554260587L;
    @SerializedName("IdAddress")
    public int mIdAddress;
    @SerializedName("StreetName")
    public String mStreetName;
    @SerializedName("StreetNumber")
    public int mStreetNumber;
    @SerializedName("HouseNumber")
    public int mHouseNumber;
    @SerializedName("City")
    public String mCity;
    @SerializedName("Postcode")
    public String mPostcode;
    @SerializedName("Latitude")
    public double mLatitude;
    @SerializedName("Longitude")
    public double mLongitude;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Address(int IdAddress, String StreetName, int StreetNumber, int HouseNumber,
            String City, String Postcode, double Latitude, double Longitude, String LastUpdate) {
        super();
        mIdAddress = IdAddress;
        mStreetName = StreetName;
        mStreetNumber = StreetNumber;
        mHouseNumber = HouseNumber;
        mCity = City;
        mPostcode = Postcode;
        mLatitude = Latitude;
        mLongitude = Longitude;
        mLastUpdate = LastUpdate;
    }

    public Address(int IdAddress, String StreetName, int StreetNumber, String City,
            String Postcode, double Latitude, double Longitude, String LastUpdate) {
        super();
        mIdAddress = IdAddress;
        mStreetName = StreetName;
        mStreetNumber = StreetNumber;
        mCity = City;
        mPostcode = Postcode;
        mLatitude = Latitude;
        mLongitude = Longitude;
        mLastUpdate = LastUpdate;
    }

    public Address() {
        // TODO Auto-generated constructor stub
    }
}
