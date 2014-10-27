
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Flavor implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4465261758795371527L;
    @SerializedName("IdFlavor")
    public int mIdFlavor;
    @SerializedName("NameEng")
    public String mNameEng;
    @SerializedName("NamePl")
    public String mNamePl;

    public Flavor(int IdFlavor, String NameEng, String NamePl) {
        super();
        mIdFlavor = IdFlavor;
        mNameEng = NameEng;
        mNamePl = NamePl;
    }
}
