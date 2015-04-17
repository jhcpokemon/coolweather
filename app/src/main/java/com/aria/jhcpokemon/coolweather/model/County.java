package com.aria.jhcpokemon.coolweather.model;

/**
 * Created by jhcpokemon on 04/17/15.
 */
public class County {
    private int id;
    private String name;
    private String code;
    private int cityID;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public int getCityID(){
        return cityID;
    }
    public void setCityID(int cityID){
        this.cityID = cityID;
    }
}
