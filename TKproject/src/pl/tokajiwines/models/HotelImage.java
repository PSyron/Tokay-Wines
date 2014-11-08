
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HotelImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4710414087318667745L;
    @SerializedName("IdHotelImage")
    public int mIdHotelImage;
    @SerializedName("IdHotel_")
    public int mIdHotel_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public HotelImage(int IdHotelImage, int IdHotel_, int IdImage_, String LastUpdate) {
        super();
        mIdHotelImage = IdHotelImage;
        mIdHotel_ = IdHotel_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }

    public HotelImage() {
        // TODO Auto-generated constructor stub
    }
}
