
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Wine;

import java.io.Serializable;

public class WineListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6693576631190121401L;
    @SerializedName("idWine")
    public int mIdWine;
    @SerializedName("idProducer")
    public int mIdProducer;
    @SerializedName("name")
    public String mName;
    @SerializedName("grade")
    public String mGrade;
    @SerializedName("year")
    public String mYear;
    @SerializedName("producerName")
    public String mProducerName;
    @SerializedName("flavourName")
    public String mFlavourName;
    @SerializedName("image")
    public String mImageUrl;
    @SerializedName("price")
    public String mPrice;
    @SerializedName("strains")
    public String mStrains;

    public WineListItem(int id, String name) {
        mIdWine = id;
        mName = name;
    }

    public WineListItem(Wine w) {
        mIdWine = w.mIdWine;
        mIdProducer = w.mIdProducer_;
        mName = w.mName;
        if (w.grade != null) mGrade = w.grade.mName;
        mYear = w.mProdDate + "";
        mProducerName = w.producer.mName;
        if (w.flavour != null) mFlavourName = w.flavour.mNameEng;
        mImageUrl = w.imageCover.mImage;
        if (w.mPrice != 0.0) mPrice = w.mPrice + "";
        if (w.strains != null) mStrains = w.strainsToString();
    }

}
