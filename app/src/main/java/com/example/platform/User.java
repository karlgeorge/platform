package com.example.platform;

/**
 * Created by 电脑配件 on 2018/8/20.
 */

import java.io.Serializable;

/**
 * Created by 电脑配件 on 2018/7/21.
 */

public class User implements Serializable {
    private String name;
    private String password;

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    private double account;
    private int id;
    private Achievement achievement;
    public User(){
        name="未命名";
        password="";
        account=0;
        id=-1;
        achievement=new Achievement();
    }
    public User(String n,String p){
        this.name=n;this.password=p;
        achievement=new Achievement();
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public int getId(){
        return id;
    }
    public void setLevel(int level){
        achievement.level=level;
    }
    public void setLanguageLevel(int l){
        achievement.languageLevel=l;
    }
    public void setExp(int e){
        achievement.exp=e;
    }
    public Achievement getAchievement(){
        return achievement;
    }
}

class Achievement implements Serializable {
    public int level;
    public int languageLevel;
    public int exp;
    public void setLevel(int level) {
        this.level = level;
    }

    public void setLanguageLevel(int languageLevel) {
        this.languageLevel = languageLevel;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public int getLanguageLevel() {
        return languageLevel;
    }

    public int getExp() {
        return exp;
    }


}