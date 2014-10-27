
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LookoutImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1049532779137907900L;
    @SerializedName("IdLookoutImage")
    public int mIdLookoutImage;
    @SerializedName("IdLookout_")
    public int mIdLookout_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public LookoutImage(int IdLookoutImage, int IdLookout_, int IdImage_, String LastUpdate) {
        super();
        mIdLookoutImage = IdLookoutImage;
        mIdLookout_ = IdLookout_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }
}
