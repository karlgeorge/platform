package com.example.platform;

import java.io.Serializable;

/**
 * Created by 电脑配件 on 2018/8/21.
 */

public class BasicTask implements Serializable {

    private User publisher;//发布者
    private int wordNumbers;//字数
    private double reward;//酬金
    private int level;//翻译者等级要求
    private String deadline;//ddl
    private String classify;//类别
    private String language;//原语言
    private String style;//形式（文本，图转文）
    private String fileName;//文件名
    private String date;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public User getPublisher() {
        return publisher;
    }

    public String getClassify() {
        return classify;
    }

    public String getLanguage() {
        return language;
    }

    public String getStyle() {
        return style;
    }

    public int getWordNumbers() {
        return wordNumbers;
    }

    public double getReward() {
        return reward;
    }

    public int getLevel() {
        return level;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setWordNumbers(int wordNumbers) {
        this.wordNumbers = wordNumbers;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return fileName+"\n"+"语言："+getLanguage()+"酬金："+getReward()+"\n发布时间："+getDate();
    }
    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fn){
        fileName=fn;
    }
}
