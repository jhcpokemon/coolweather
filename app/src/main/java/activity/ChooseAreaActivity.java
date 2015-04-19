package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aria.jhcpokemon.coolweather.R;
import com.aria.jhcpokemon.coolweather.db.WeatherDatabase;
import com.aria.jhcpokemon.coolweather.model.City;
import com.aria.jhcpokemon.coolweather.model.County;
import com.aria.jhcpokemon.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utility;

/**
 * Created by jhcpokemon on 04/18/15.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDatabase weatherDatabase;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        textView = (TextView)findViewById(R.id.title_text);
        listView = (ListView)findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        weatherDatabase = WeatherDatabase.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = weatherDatabase.loadProvinces();
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province p : provinceList){
                dataList.add(p.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            textView.setText(R.string.china);
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServe(null, "province");
        }
    }
    private void queryCities(){
        cityList = weatherDatabase.loadCities(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City c : cityList){
                dataList.add(c.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            textView.setText(selectedProvince.getName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServe(selectedProvince.getCode(),"city");
        }
    }
    private void queryCounties(){
        countyList = weatherDatabase.loadCounties(selectedCity.getId());
        if(countyList.size() > 0){
            dataList.clear();
            for(County c : countyList){
                dataList.add(c.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            textView.setText(selectedCity.getName());
            currentLevel = LEVEL_COUNTY;
        }else {
            queryFromServe(selectedCity.getCode(),"county");
            Log.e("Main", selectedCity.getCode());
        }
    }

    private void queryFromServe(final String code,final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://10.0.3.2/city"+ code + ".xml";
        }else {
            address = "http://10.0.3.2/city.xml";
        }
        showProgressDialog();
        HttpUtil.accessUrl(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(weatherDatabase,response);
                }else if("city".equals(type)){
                    result = Utility.handleCitiesResponse(weatherDatabase,response,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountiesResponse(weatherDatabase,response,selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        textView.setText(R.string.fail);
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        if(currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {finish();}
    }
}
