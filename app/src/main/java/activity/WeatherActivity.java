package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aria.jhcpokemon.coolweather.R;

import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utility;

/**
 * Created by jhcpokemon on 04/21/15.
 */
public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView currentDateText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private Button switch_city;
    private Button fresh;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout  = (LinearLayout)findViewById(R.id.weather_info);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        currentDateText = (TextView)findViewById(R.id.current_date);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        switch_city = (Button)findViewById(R.id.switch_city);
        fresh = (Button)findViewById(R.id.fresh);
        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText(R.string.sync);
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else {
            showWeather();
        }
        switch_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishText.setText(R.string.loading);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });
    }

    public void queryWeatherCode(String countyCode){
        String address = "http://10.0.3.2/city"+ countyCode + ".xml";
        queryFromServe(address, "countyCode");
    }

    public void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        publishText.setText(prefs.getString("今天" + "publish_time" + "发布",""));
        currentDateText.setText(prefs.getString("current_date",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        cityNameText.setVisibility(View.VISIBLE);
        weatherInfoLayout.setVisibility(View.VISIBLE);

    }

    public void queryFromServe(final String address,final String type){
        HttpUtil.accessUrl(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                if("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array.length >= 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if("weatherCode".equals(type)){
                        Utility.handleWeatherResponse(WeatherActivity.this, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }

            @Override
            public void onError(Exception e) {
                publishText.setText(R.string.fail);
            }
        });
    }

    public void queryWeatherInfo(String weatherCode){
        String address = "http://10.0.3.2/"+ weatherCode + ".json";
        queryFromServe(address, "weatherCode");
    }
}
