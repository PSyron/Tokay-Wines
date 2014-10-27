
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5558320785169053736L;
    @SerializedName("IdRestaurantImage")
    public int mIdRestaurantImage;
    @SerializedName("IdRestaurant_")
    public int mIdRestaurant_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public RestaurantImage(int IdRestaurantImage, int IdRestaurant_, int IdImage_, String LastUpdate) {
        super();
        mIdRestaurantImage = IdRestaurantImage;
        mIdRestaurant_ = IdRestaurant_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }
}
