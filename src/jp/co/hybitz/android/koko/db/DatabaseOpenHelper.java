/**
 *
 */
package jp.co.hybitz.android.koko.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @author hiro
 *
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "koko.db";
    private static final int DB_VERSION = 1;

    public DatabaseOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    /* (非 Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try{
            StringBuilder createSql = new StringBuilder();
            createSql.append("create table " + LocationInfo.TABLE_NAME + " (");
            createSql.append(LocationInfo.COLUMN_ID + " integer primary key autoincrement not null,");
            createSql.append(LocationInfo.COLUMN_TIME_AND_DATE + " text not null,");
            createSql.append(LocationInfo.COLUMN_LATITUDE + " text not null,");
            createSql.append(LocationInfo.COLUMN_LONGITUDE + " text not null");
            createSql.append(")");

            db.execSQL(createSql.toString());
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
    }

    /* (非 Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
