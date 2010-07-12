/**
 *
 */
package jp.co.hybitz.android.koko;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.co.hybitz.android.koko.db.LocationDao;
import jp.co.hybitz.android.koko.db.LocationInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author hiro
 *
 */
public class ListActivity extends Activity implements OnItemClickListener{

    private ListView listView = null;
    private ArrayAdapter<LocationInfo> arrayAdapter = null;

    /* (非 Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView)findViewById(R.id.list);

        arrayAdapter = new ArrayAdapter<LocationInfo>(this,
                android.R.layout.test_list_item);
        // 要素を追加
        //arrayAdapter.add("新規追加");
        listView.setAdapter(arrayAdapter);
        // listener
        listView.setOnItemClickListener(this);
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocationDao dao = new LocationDao(ListActivity.this);
        List<LocationInfo> result = dao.list();
        // 表示データのクリア
        arrayAdapter.clear();
        for(LocationInfo locationInfo : result){
            arrayAdapter.add(locationInfo);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LocationInfo locationInfo = (LocationInfo)parent.getItemAtPosition(position);
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra(LocationInfo.TABLE_NAME, locationInfo);
        startActivity(mapIntent);
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return true;
    }

    /* (非 Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
        case R.id.menu_start:
            Intent startIntent = new Intent( this, LocationActivity.class);
            startActivity( startIntent);
            break;
        case R.id.menu_clear:
            LocationDao dao = new LocationDao(ListActivity.this);
            dao.deleteAll();
            // 表示データのクリア
            arrayAdapter.clear();
            break;
        case R.id.menu_demo:
            insertDemoData();
            arrayAdapter.clear();
            listView = (ListView)findViewById(R.id.list);
            arrayAdapter = new ArrayAdapter<LocationInfo>(this,
                    android.R.layout.test_list_item);
            listView.setAdapter(arrayAdapter);
            break;
        }
        return true;
    }


    private String[][] sample_data = {{"2010/07/01","35.62855861247704","139.73931312561035"},
                                      {"2010/07/02","35.61879104394229","139.73450660705566"},
                                      {"2010/07/03","35.6254889337936","139.72412109375"},
                                      {"2010/07/04","35.633651228488","139.73158836364746"},
                                      {"2010/07/05","35.644951491387054","139.73871231079102"},
                                      {"2010/07/06","35.64209169933934","139.7475528717041"},
                                      {"2010/07/07","35.64285897066725","139.755277633667"},
                                      {"2010/07/08","35.652484112208924","139.7599983215332"},
                                      {"2010/07/09","34.97455975602553","139.09202098846436"}};

    private void insertDemoData(){
        LocationDao dao = new LocationDao(ListActivity.this);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient( true );
        sdf.applyPattern( "yyyy/MM/dd" );
        try{
            for(int i = 0 ; i < sample_data.length ; i++){
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setTimeAndDate((sdf.parse(sample_data[i][0])).getTime());
                locationInfo.setLatitude(Double.valueOf(sample_data[i][1]));
                locationInfo.setLongitude(Double.valueOf(sample_data[i][2]));
                dao.save(locationInfo);
            }
        }catch(ParseException e){
        }

    }
}
