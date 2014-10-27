
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Grade implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4311045291783252771L;

    @SerializedName("IdGrade")
    public int mIdGrade;
    @SerializedName("Name")
    public String mName;

    public Grade(int IdGrade, String Name) {
        super();
        mIdGrade = IdGrade;
        mName = Name;
    }

}
