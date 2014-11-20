
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CuriosityImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 53955924614542260L;
    @SerializedName("IdCuriosityImage")
    public int mIdCuriosityImage;
    @SerializedName("IdCuriosity_")
    public int mIdCuriosity_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public CuriosityImage(int IdCuriosityImage, int IdCuriosity_, int IdImage_, String LastUpdate) {
        super();
        mIdCuriosityImage = IdCuriosityImage;
        mIdCuriosity_ = IdCuriosity_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }

    public CuriosityImage() {
        // TODO Auto-generated constructor stub
    }
}
