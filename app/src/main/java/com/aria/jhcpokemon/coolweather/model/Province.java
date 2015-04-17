package com.aria.jhcpokemon.coolweather.model;

/**
 * Created by jhcpokemon on 04/17/15.
 */
public class Province{
    private int id;
    private String name;
    private String code;
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
}
