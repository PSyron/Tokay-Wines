package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

public class ProducerListItem {
    
    @SerializedName("idProducer")
    public int mIdProducer;
    @SerializedName("name")
    public String mName;
    @SerializedName("short")
    public String mDescription;
    
    public ProducerListItem(int id, String name, String description)
    {
        mIdProducer = id;
        mName = name;
        mDescription = description;
    }
    

}
