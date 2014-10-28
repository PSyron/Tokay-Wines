
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

public class HotelsListItem {

    @SerializedName("idHotel")
    public int mIdHotel;
    @SerializedName("name")
    public String mName;
    @SerializedName("phone")
    public String mPhone;
    @SerializedName("image")
    public String mImageUrl;

    public HotelsListItem(int id, String name, String phone) {
        mIdHotel = id;
        mName = name;
        mPhone = phone;
    }

}
