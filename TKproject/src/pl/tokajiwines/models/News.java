
package pl.tokajiwines.models;

import java.io.Serializable;

public class News implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2308726436283474494L;
    public String header;
    public String vast;
    public String entryDate;
    public String startDate;
    public String endDate;
    public String image;

/*    public News(String t, String c) {
        Title = t;
        Content = c;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getContent() {
        return this.Content;
    }*/

}
