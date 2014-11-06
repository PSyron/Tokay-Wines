package pl.tokajiwines.jsonresponses;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class WineListItem implements Serializable{

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
    


}
