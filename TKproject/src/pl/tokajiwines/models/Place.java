
package pl.tokajiwines.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Place implements Serializable {

    private static final long serialVersionUID = 5072875659435615059L;
    /**
     * 
     */
    //{"idPlace":"1","name":"Toldi Fogad\u00f3","address":"Hajd\u00fa k\u00f6z 2 Tokaj","longitude":"21.4106","latitude":"48.1238","placeType":"Hotel"
    @SerializedName("idPlace")
    public int mIdPlace;
    @SerializedName("name")
    public String mName;
    @SerializedName("address")
    public String mAddress;
    @SerializedName("longitude")
    public String mLng;
    @SerializedName("latitude")
    public String mLat;
    @SerializedName("placeType")
    public String mPlaceType;
    @SerializedName("phone")
    public String mPhone;
    @SerializedName("imageUrl")
    public String mImageUrl;

    public Place(int idPlace, String name, String address, String lng, String lat, String placeType) {
        super();
        mIdPlace = idPlace;
        mName = name;
        mAddress = address;
        mLng = lng;
        mLat = lat;
        mPlaceType = placeType;

    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
    }

}
