package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

public class SearchItem {
    
    @SerializedName("id")
    public int mId;
    @SerializedName("type")
    public int mItemType;
    @SerializedName("name")
    public String mName;
    
    
    public SearchItem(int i, int t, String n)
    {
        mId = i;
        mItemType = t;
        mName = n;
    }
    

}
