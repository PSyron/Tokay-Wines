
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
    public int IdDescription_;
    @SerializedName("Label")
    public int mLabel;
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

    public Wine(int IdWine, String Name, int ProdDate, double Price, int Label, int Volume,
            String LastUpdate, int IdColor_, int IdFlavour_, int IdProducer_, int IdDescription_,
            int IdGrade_, int IdImageCover_) {
        super();
        mIdWine = IdWine;
        mName = Name;
        mProdDate = ProdDate;
        mPrice = Price;
        mLabel = Label;
        mVolume = Volume;
        mLastUpdate = LastUpdate;
        mIdColor_ = IdColor_;
        mIdFlavour_ = IdFlavour_;
        mIdProducer_ = IdProducer_;
        mIdDescription_ = IdDescription_;
        mIdGrade_ = IdGrade_;
        mIdImageCover_ = IdImageCover_;
    }

}
