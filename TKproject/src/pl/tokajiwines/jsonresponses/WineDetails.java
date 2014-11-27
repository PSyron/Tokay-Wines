package pl.tokajiwines.jsonresponses;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class WineDetails implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -6246309935269188015L;
    
    @SerializedName("success")    
    public String mSuccess;   
    @SerializedName("message")    
    public String mMessage;    
    @SerializedName("image")    
    public String mImageUrl;
    @SerializedName("description")
    public String mDescription;
    @SerializedName("basics")
    public WineListItem mBasics;

}
