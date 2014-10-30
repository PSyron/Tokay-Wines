
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

public class RestaurantsListItem {

    // TODO Auto-generated constructor stub
    @SerializedName("idRestaurant")
    public int mIdRestaurant;
    @SerializedName("name")
    public String mName;
    @SerializedName("phone")
    public String mPhone;
    @SerializedName("image")
    public String mImageUrl;

    public RestaurantsListItem(int id, String name, String phone) {
        mIdRestaurant = id;
        mName = name;
        mPhone = phone;
    }

}
