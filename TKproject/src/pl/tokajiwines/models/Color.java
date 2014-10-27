
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Color implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1435688259992025772L;

    @SerializedName("IdColor")
    public int mIdColor;
    @SerializedName("NameEng")
    public String mNameEng;
    @SerializedName("NamePl")
    public String mNamePl;

    public Color(int IdColor, String NameEng, String NamePl) {
        super();
        mIdColor = IdColor;
        mNameEng = NameEng;
        mNamePl = NamePl;
    }
}
