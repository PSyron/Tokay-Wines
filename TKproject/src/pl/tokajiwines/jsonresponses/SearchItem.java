package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

public class SearchItem {
    
    @SerializedName("id")
    public int mId;
    @SerializedName("type")
    public String mItemType;
    @SerializedName("name")
    public String mName;
    
    
    public SearchItem(int i, String t, String n)
    {
        mId = i;
        mItemType = t;
        mName = n;
    }
    

}
