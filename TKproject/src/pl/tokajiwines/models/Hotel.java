
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hotel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5969538435697362052L;

    @SerializedName("IdHotel")
    public int mIdHotel;
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
    @SerializedName("IdDescription_")
    public int mIdDescription_;

    public Description description;

    public Address address;

    public Image imageCover;

    public Hotel(int IdHotel, String Email, String Link, String Name, String Phone,
            int IdDescription_, int IdAddress_, int IdUser_, int IdImageCover_, String LastUpdate) {
        super();
        mIdHotel = IdHotel;
        mEmail = Email;
        mLink = Link;
        mName = Name;
        mPhone = Phone;
        mIdDescription_ = IdDescription_;
        mIdAddress_ = IdAddress_;
        mIdUser_ = IdUser_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public Hotel() {
        // TODO Auto-generated constructor stub
    }

}
