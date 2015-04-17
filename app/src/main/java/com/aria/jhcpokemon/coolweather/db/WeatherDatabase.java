package com.aria.jhcpokemon.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aria.jhcpokemon.coolweather.model.City;
import com.aria.jhcpokemon.coolweather.model.County;
import com.aria.jhcpokemon.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhcpokemon on 04/17/15.
 */
public class WeatherDatabase {
    public static final String DB_NAME = "weather.db";
    public static final int VERSION = 1;
    private static WeatherDatabase weatherDB;
    private SQLiteDatabase db;
    private WeatherDatabase(Context context){
        WeatherOpenHelper dbHelper = new WeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /**»ñÈ¡WeatherDatabaseÊµÀý*/
    public synchronized static WeatherDatabase getInstance(Context context){
        if(weatherDB == null){
            weatherDB = new WeatherDatabase(context);
        }
        return weatherDB;
    }
    /**´æ´¢Province*/
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getName());
            values.put("province_code",province.getCode());
            db.insert("Province",null,values);
        }
    }
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        cursor.close();
    return list;
    }

    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getName());
            values.put("city_code",city.getCode());
            values.put("province_id",city.getProvinceID());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceID(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getName());
            values.put("county_code",county.getCode());
            values.put("city_id",county.getCityID());
            db.insert("County",null,values);
        }
    }

    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityID(cityId);
                list.add(county);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
