
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonumentImage implements Serializable {

    private static final long serialVersionUID = -2528472937038004482L;
    /**
     * 
     */
    @SerializedName("IdMonumentImage")
    public int mIdMonumentImage;
    @SerializedName("IdMonument_")
    public int mIdMonument_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public MonumentImage(int IdMonumentImage, int IdMonument_, int IdImage_, String LastUpdate) {
        super();
        mIdMonumentImage = IdMonumentImage;
        mIdMonument_ = IdMonument_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }
}
