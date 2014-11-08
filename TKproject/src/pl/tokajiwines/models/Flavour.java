
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Flavour implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4465261758795371527L;
    @SerializedName("IdFlavour")
    public int mIdFlavour;
    @SerializedName("NameEng")
    public String mNameEng;
    @SerializedName("NamePl")
    public String mNamePl;

    public Flavour(int IdFlavour, String NameEng, String NamePl) {
        super();
        mIdFlavour = IdFlavour;
        mNameEng = NameEng;
        mNamePl = NamePl;
    }

    public Flavour() {
        // TODO Auto-generated constructor stub
    }
}
