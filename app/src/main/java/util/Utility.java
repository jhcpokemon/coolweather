package util;

import android.text.TextUtils;
import android.util.Log;

import com.aria.jhcpokemon.coolweather.db.WeatherDatabase;
import com.aria.jhcpokemon.coolweather.model.City;
import com.aria.jhcpokemon.coolweather.model.County;
import com.aria.jhcpokemon.coolweather.model.Province;

/**
 * Created by jhcpokemon on 04/18/15.
 */
public class Utility {
    public synchronized static boolean handleProvinceResponse(WeatherDatabase weatherDatabase,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces.length > 0){
                for(String p:allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setCode(array[0]);
                    province.setName(array[1]);
                    weatherDatabase.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(WeatherDatabase weatherDatabase,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities.length > 0){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCode(array[0]);
                    city.setName(array[1]);
                    city.setProvinceID(provinceId);
                    weatherDatabase.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized  static boolean handleCountiesResponse(WeatherDatabase weatherDatabase,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if (allCounties.length > 0){
                for(String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCode(array[0]);
                    county.setName(array[1]);
                    county.setCityID(cityId);
                    weatherDatabase.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
