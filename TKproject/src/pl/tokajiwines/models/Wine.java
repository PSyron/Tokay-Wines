
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wine implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 715542698982651737L;

    @SerializedName("IdWine")
    public int mIdWine;
    @SerializedName("Name")
    public String mName;
    @SerializedName("ProdDate")
    public int mProdDate;
    @SerializedName("Price")
    public double mPrice;
    @SerializedName("AvailablePL")
    public int mAvailablePL;
    @SerializedName("Volume")
    public int mVolume;
    @SerializedName("LastUpdate")
    public String mLastUpdate;
    @SerializedName("IdColor_")
    public int mIdColor_;
    @SerializedName("IdFlavour_")
    public int mIdFlavour_;
    @SerializedName("IdProducer_")
    public int mIdProducer_;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdGrade_")
    public int mIdGrade_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;

    public Image big;
    public Image imageCover;
    public Producer producer;
    public Grade grade;
    public WineStrain[] strains;
    public Description description;
    public Color color;
    public Flavour flavour;

    public Wine(int IdWine, String Name, int ProdDate, double Price, int AvailablePL, int Volume,
            String LastUpdate, int IdColor_, int IdFlavour_, int IdProducer_, int IdDescription_,
            int IdGrade_, int IdImageCover_) {
        super();
        mIdWine = IdWine;
        mName = Name;
        mProdDate = ProdDate;
        mPrice = Price;
        mAvailablePL = AvailablePL;
        mVolume = Volume;
        mLastUpdate = LastUpdate;
        mIdColor_ = IdColor_;
        mIdFlavour_ = IdFlavour_;
        mIdProducer_ = IdProducer_;
        mIdDescription_ = IdDescription_;
        mIdGrade_ = IdGrade_;
        mIdImageCover_ = IdImageCover_;
    }

    public Wine() {
        // TODO Auto-generated constructor stub
    }

    public String strainsToString() {
        String strain = "";
        if (strains != null) {
            for (int i = 0; i < strains.length; i++) {
                strain += strains[i].strain.mName + " ";
                if (strains[i].mContent != 0) strain += strains[i].mContent + "%";
                if (i < strains.length - 1) strain += "\n";
            }
        }
        return strain;
    }
}
