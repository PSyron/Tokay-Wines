
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Search implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1892752740272069444L;

    @SerializedName("Id")
    public int mId;
    @SerializedName("IdSearch")
    public int mIdSearch;
    @SerializedName("Name")
    public String mName;
    @SerializedName("SearchType")
    public String mSearchType;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Search(int Id, int Search, String Name, String LastUpdate) {
        super();
        mId = Id;
        mIdSearch = Search;
        mName = Name;
        mLastUpdate = LastUpdate;
    }

    public Search() {
        // TODO Auto-generated constructor stub
    }

}
