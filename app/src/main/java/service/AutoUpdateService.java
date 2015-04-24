package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import activity.WeatherActivity;
import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utility;

/**
 * Created by jhcpokemon on 04/24/15.
 */
public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000;
        long triggetAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent1 = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent1,0);
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggetAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    public void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = prefs.getString("weather_code", "");
        String address = "http://10.0.3.2/"+ weatherCode + ".json";
        HttpUtil.accessUrl(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
