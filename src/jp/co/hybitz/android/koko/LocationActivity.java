package jp.co.hybitz.android.koko;

import java.util.Calendar;
import java.util.Date;

import jp.co.hybitz.android.koko.db.LocationDao;
import jp.co.hybitz.android.koko.db.LocationInfo;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LocationActivity extends Activity implements LocationListener {
    private LocationManager mLocationManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Log.v("Status:", "Call onCreate");
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        if(mLocationManager != null){
            mLocationManager.removeUpdates(this);
        }
        super.onPause();
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        Log.v("Status:", "Call onResume");
        if(mLocationManager != null){
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0, // 通知のための最小時間間隔
                    0, // 通知のための最小距離間隔
                    this); // 位置情報リスナー
        }
        super.onResume();
    }

    public void onLocationChanged(Location location) {
        Log.v("Status:", "Call onLocationChanged");
        Log.v("----------", "----------");
        Log.v("Latitude:", String.valueOf(location.getLatitude())); // 緯度取得
        Log.v("Longitude:", String.valueOf(location.getLongitude())); // 経度取得
        Log.v("Accuracy:", String.valueOf(location.getAccuracy())); // 精度取得
        Log.v("Altitude:", String.valueOf(location.getAltitude())); // 標高取得
        Date d = new Date(location.getTime());
        Log.v("Time:", String.valueOf(location.getTime())); // 時間取得
        Log.v("Date:", d.toString());
        Calendar cal = Calendar.getInstance();
        Date d2 = new Date(cal.getTimeInMillis());
        Log.v("Date2:", d2.toString());
        Log.v("Speed", String.valueOf(location.getSpeed()));
        Log.v("Bearing", String.valueOf(location.getBearing()));
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setTimeAndDate(cal.getTimeInMillis());
        locationInfo.setLatitude(location.getLatitude());
        locationInfo.setLongitude(location.getLongitude());
        Log.v("Status:", "Create instance Dao");
        LocationDao dao = new LocationDao(LocationActivity.this);
        Log.v("Status:", "Save!");
        dao.save(locationInfo);
    }

    public void onProviderDisabled(String provider) {
        Log.v("Status:", "Call onProviderDisabled");
    }

    public void onProviderEnabled(String provider) {
        Log.v("Status:", "Call onProviderEnabled");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("Status:", "Call onStatusChanged");
        switch (status){
        case LocationProvider.AVAILABLE:
            Log.v("Status:", "AVAILABLE");
            break;
        case LocationProvider.OUT_OF_SERVICE:
            Log.v("Status:", "OUT_OF_SERVICE");
            break;
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
            Log.v("Status:", "TEMPORARILY_UNAVAILABLE");
            break;
        }
    }



    /* (非 Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location, menu);
        return true;
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
        case R.id.menu_back:
            //Intent backIntent = new Intent( this, ListActivity.class);
            //startActivity( backIntent);
            finish();
            break;
        }
        return true;
    }

}