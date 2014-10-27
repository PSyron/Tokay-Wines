
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WineStrain implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9015053927179377164L;

    @SerializedName("IdWineStrain")
    public int mIdWineStrain;
    @SerializedName("IdWine")
    public int mIdWine;
    @SerializedName("Content")
    public int mContent;
    @SerializedName("IdStrain")
    public int mIdStrain;
    @SerializedName("Name")
    public String mName;

    public WineStrain(int IdWineStrain, int Content, int IdWine, int IdStrain, String Name) {
        super();
        mIdWineStrain = IdWineStrain;
        mContent = Content;
        mIdWine = IdWine;
        mIdStrain = IdStrain;
        mName = Name;
    }

}
