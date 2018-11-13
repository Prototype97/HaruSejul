package com.sierramatcher.harusejul;

public class SejulItem {

    //리스트뷰 아이템종류 2개
    int type;

    //yearmonth_item
    String year;
    String month;

    //sejul_item
    String date;
    String day;
    String good;
    String bad;
    String will;

    //생성자
    public SejulItem(int type, String year, String month, String date, String day, String good, String bad, String will) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
        this.good = good;
        this.bad = bad;
        this.will = will;
    }

    public SejulItem(String date, String day, String good, String bad, String will) {
        this.date = date;
        this.day = day;
        this.good = good;
        this.bad = bad;
        this.will = will;
    }

    public SejulItem(String year, String month) {
        this.year = year;
        this.month = month;
    }

    //게터 앤 세터
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getBad() {
        return bad;
    }

    public void setBad(String bad) {
        this.bad = bad;
    }

    public String getWill() {
        return will;
    }

    public void setWill(String will) {
        this.will = will;
    }
}