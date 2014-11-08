
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OddityImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 53955924614542260L;
    @SerializedName("IdOddityImage")
    public int mIdOddityImage;
    @SerializedName("IdOddity_")
    public int mIdOddity_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public OddityImage(int IdOddityImage, int IdOddity_, int IdImage_, String LastUpdate) {
        super();
        mIdOddityImage = IdOddityImage;
        mIdOddity_ = IdOddity_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }

    public OddityImage() {
        // TODO Auto-generated constructor stub
    }
}
