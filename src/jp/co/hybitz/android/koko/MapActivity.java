/**
 *
 */
package jp.co.hybitz.android.koko;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import jp.co.hybitz.android.koko.db.LocationDao;
import jp.co.hybitz.android.koko.db.LocationInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author hiro
 *
 */
public class MapActivity extends com.google.android.maps.MapActivity implements OnCheckedChangeListener{

    private LocationInfo locationInfo = null;
    // UI部品
    private MapView map = null;
    private RadioGroup radioGroup = null;
    private RadioButton normalMapRadio = null;
    private ToggleButton currentLocationToggle = null;

    private MapController controller = null;


    // デフォルトのズーム値
    protected int defaultZoom = 15;

    // 目的地表示Overlay
    protected DistinationOverlay distLocationOverlay = null;

    /* (非 Javadoc)
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 自動生成されたR.javaの定数を指定してXMLからレイアウトを生成
        setContentView(R.layout.map);
        map = (MapView) findViewById(R.id.map);
        normalMapRadio = (RadioButton)findViewById(R.id.normalMapRadio);
        normalMapRadio.setChecked( true);
        radioGroup = (RadioGroup)findViewById( R.id.mapRadioGroup);

        radioGroup.setOnCheckedChangeListener( this);
        currentLocationToggle = (ToggleButton)findViewById( R.id.currentLocationToggle);

        controller = map.getController();
        // ZoomControlの利用
        map.setBuiltInZoomControls( true);
        map.setSatellite( false);
        map.setClickable(true);
        map.setEnabled(true);


        // get location info from intent
        Intent intent = getIntent();
        locationInfo = (LocationInfo)intent.getSerializableExtra(LocationInfo.TABLE_NAME);
        if(locationInfo != null){
            setLocation(locationInfo);
        }

        Log.v("Map log:", locationInfo.getRowid().toString());
    }

    private void setLocation(LocationInfo locationInfo) {
        // DEG表記からGeoPoint形式に変換（100万倍）
        Double latitude = locationInfo.getLatitude() * 1E6;
        Double longitude = locationInfo.getLongitude() * 1E6;
        GeoPoint distPoint = new GeoPoint( latitude.intValue(), longitude.intValue());
        // 目的地をマップの中心にする。
        controller.animateTo( distPoint);
        // ズームをデフォルト設定
        controller.setZoom( defaultZoom);
        // 目標値の座標にイメージを描画
        if( distLocationOverlay == null){
            // 初回はOverlayを作成
            Bitmap bmp = BitmapFactory.decodeResource( getResources(), R.drawable.car);
            distLocationOverlay = new DistinationOverlay(bmp);
            distLocationOverlay.setGeoPoint( distPoint);
            // Overlayの追加
            map.getOverlays().add( distLocationOverlay);
        }
        else{
            // 2回目以降はOverlayを再利用
            distLocationOverlay.setGeoPoint( distPoint);
            // 無効にして再描画
            map.invalidate();
        }
    }

    /* (非 Javadoc)
     * @see com.google.android.maps.MapActivity#isRouteDisplayed()
     */
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }



    /* (非 Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
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
            finish();
            break;
        case R.id.menu_next:
            if(locationInfo != null){
                LocationDao dao = new LocationDao(MapActivity.this);
                LocationInfo locationInfo_tmp = null;
                long rowId = locationInfo.getRowid();
                rowId++;
                locationInfo_tmp = dao.load(rowId);
                if(locationInfo_tmp != null){
                    Log.v("Status", "info is not null");
                    locationInfo = locationInfo_tmp;
                    setLocation(locationInfo);
                }
            }
            break;
        case R.id.menu_prev:
            if(locationInfo != null){
                LocationInfo locationInfo_tmp = null;
                long rowId = locationInfo.getRowid();
                rowId--;
                LocationDao dao2 = new LocationDao(MapActivity.this);
                locationInfo_tmp = dao2.load(rowId);
                if(locationInfo_tmp != null){
                    locationInfo = locationInfo_tmp;
                    setLocation(locationInfo);
                }
            }
            break;
        }
        return true;
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if( checkedId == R.id.normalMapRadio){
            // 衛星写真OFF
            map.setSatellite( false);
        }
        else if( checkedId == R.id.satelliteMapRadio){
            // 衛星写真ON
            map.setSatellite( true);
        }
    }


    /**
     * 目的地描画用Overlay
     */
    public class DistinationOverlay extends Overlay {

        // 描画するイメージ
        private Bitmap bmp;
        // 描画する座標
        private GeoPoint geoPoint;

        /**
         * コンストラクタ
         * @param bmp 描画するイメージ
         */
        public DistinationOverlay( Bitmap bmp) {
            this.bmp = bmp;
        }

        /**
         * 描画する座標の取得
         * @return 描画する座標
         */
        public GeoPoint getGeoPoint() {
            return geoPoint;
        }

        /**
         * 描画する座標の設定
         * @param geoPoint 描画する座標
         */
        public void setGeoPoint(GeoPoint geoPoint) {
            this.geoPoint = geoPoint;
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            // shadow = true,falseで2回呼び出される
            if( !shadow){
                // Mapの表示位置と座標から画面の描画位置を算出
                Projection pro = mapView.getProjection();
                Point p = pro.toPixels(geoPoint, null);

                // 描画(目的地がピンの先に来るようにy座標を調整)
                int centerX = p.x;
                int centerY = p.y - bmp.getHeight();
                // 画像の描画
                canvas.drawBitmap(bmp, centerX, centerY, null);
            }
        }
    }


}
