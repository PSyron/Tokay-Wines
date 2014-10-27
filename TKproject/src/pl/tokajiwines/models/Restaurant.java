
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private static final long serialVersionUID = 3167694164535978686L;
    /**
     * 
     */
    @SerializedName("IdRestaurant")
    public int mIdRestaurant;
    @SerializedName("Email")
    public String mEmail;
    @SerializedName("Link")
    public String mLink;
    @SerializedName("Name")
    public String mName;
    @SerializedName("Phone")
    public String mPhone;
    @SerializedName("IdAddress_")
    public int mIdAddress_;
    @SerializedName("IdUser_")
    public int mIdUser_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Restaurant(int IdRestaurant, String Email, String Link, String Name, String Phone,
            int IdAddress_, int IdUser_, int IdImageCover_, String LastUpdate) {
        super();
        mIdRestaurant = IdRestaurant;
        mEmail = Email;
        mLink = Link;
        mName = Name;
        mPhone = Phone;
        mIdAddress_ = IdAddress_;
        mIdUser_ = IdUser_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

}
