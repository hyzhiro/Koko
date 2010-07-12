/**
 *
 */
package jp.co.hybitz.android.koko.db;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hiro
 *
 */
public class LocationInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // Table name
    public static final String TABLE_NAME = "location_info";
    // Column name
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME_AND_DATE = "time_and_date";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private Long rowid = null;
    private Long timeAndDate = null;
    private Double latitude = null;
    private Double longitude = null;



    /**
     * @return rowid
     */
    public Long getRowid() {
        return rowid;
    }



    /**
     * @param rowid セットする rowid
     */
    public void setRowid(Long rowid) {
        this.rowid = rowid;
    }



    /**
     * @return timeAndDate
     */
    public Long getTimeAndDate() {
        return timeAndDate;
    }



    /**
     * @param timeAndDate セットする timeAndDate
     */
    public void setTimeAndDate(Long timeAndDate) {
        this.timeAndDate = timeAndDate;
    }




    /**
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }



    /**
     * @param latitude セットする latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



    /**
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }



    /**
     * @param longitude セットする longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    /* (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(rowid != null){
            Date d = new Date(timeAndDate);
            builder.append(rowid.toString() + ",");
            builder.append(d.toString() + ",");
            builder.append(latitude + ",");
            builder.append(longitude);
        }
        return builder.toString();
    }


}
