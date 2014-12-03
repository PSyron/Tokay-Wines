
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
    @SerializedName("IdWine_")
    public int mIdWine_;
    @SerializedName("Content")
    public int mContent;
    @SerializedName("IdStrain_")
    public int mIdStrain_;

    public Strain strain;

    public WineStrain(int IdWineStrain, int Content, int IdWine_, int IdStrain_) {
        mIdWineStrain = IdWineStrain;
        mContent = Content;
        mIdWine_ = IdWine_;
        mIdStrain_ = IdStrain_;
    }

    public WineStrain() {
        // TODO Auto-generated constructor stub
    }

}
