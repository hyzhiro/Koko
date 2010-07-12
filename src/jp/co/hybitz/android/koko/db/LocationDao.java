/**
 *
 */
package jp.co.hybitz.android.koko.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author hiro
 *
 */
public class LocationDao {
    private DatabaseOpenHelper helper = null;
    private static final int MAX_ROWS = 100;
    /**
     *
     */
    public LocationDao(Context context) {
        helper = new DatabaseOpenHelper(context);
    }

    /**
     * save location info
     * @param locationInfo
     * @return location info
     */
    public LocationInfo save(LocationInfo locationInfo){
        SQLiteDatabase db = helper.getWritableDatabase();
        LocationInfo result = null;
        try{
            ContentValues values = new ContentValues();
            values.put(LocationInfo.COLUMN_TIME_AND_DATE, locationInfo.getTimeAndDate());
            values.put(LocationInfo.COLUMN_LATITUDE, locationInfo.getLatitude().toString());
            values.put(LocationInfo.COLUMN_LONGITUDE, locationInfo.getLongitude().toString());
            Long rowId = locationInfo.getRowid();
            if(rowId == null){
                rowId = db.insert(LocationInfo.TABLE_NAME, null, values);
            }else{
                db.update(LocationInfo.TABLE_NAME, values,
                        LocationInfo.COLUMN_ID + "=?", new String[]{String.valueOf(rowId)});
            }
            result = load(rowId);

            // limit rows
            List<LocationInfo> locationList = list();
            int rows = locationList.size();
            while(MAX_ROWS < rows){
                delete(locationList.get(rows - MAX_ROWS - 1));
                rows--;
            }

        }finally{
            db.close();
        }
        return result;
    }

    public LocationInfo load(Long rowId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        LocationInfo locationInfo = null;
        try{
            Cursor cursor = db.query(LocationInfo.TABLE_NAME,
                                     null,
                                     LocationInfo.COLUMN_ID + "=?",
                                     new String[]{String.valueOf(rowId)},
                                     null, null, null);
            cursor.moveToFirst();
            locationInfo = getLocationInfo(cursor);
            cursor.close();
        }catch(Exception e){
        }finally{
            db.close();
        }
        return locationInfo;
    }

    /**
     * delete location info
     * @param locationInfo
     */
    public void delete(LocationInfo locationInfo){
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            db.delete(LocationInfo.TABLE_NAME,LocationInfo.COLUMN_ID + "=?",
                    new String[]{String.valueOf(locationInfo.getRowid())});
        }finally{
            db.close();
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            List<LocationInfo> locationList = list();
            for(int i = 0; i < locationList.size(); i++){
                delete(locationList.get(i));
            }
        }finally{
            db.close();
        }
    }

    /**
     * List of location info. sort by column id.
     * @return
     */
    public List<LocationInfo> list(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<LocationInfo> locationList;
        try{
            Cursor cursor = db.query(LocationInfo.TABLE_NAME,
                                     null, null, null, null, null, LocationInfo.COLUMN_ID);
            locationList = new ArrayList<LocationInfo>();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                locationList.add(getLocationInfo(cursor));
                cursor.moveToNext();
            }
        }finally{
            db.close();
        }
        return locationList;
    }

    /**
     * @param cursor
     * @return location info
     */
    private LocationInfo getLocationInfo(Cursor cursor) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setRowid(cursor.getLong(0));
        locationInfo.setTimeAndDate(cursor.getLong(1));
        locationInfo.setLatitude(cursor.getDouble(2));
        locationInfo.setLongitude(cursor.getDouble(3));
        return locationInfo;
    }
}
