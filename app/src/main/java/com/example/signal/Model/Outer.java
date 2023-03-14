package com.example.signal.Model;

import java.util.List;



public class Outer {
    private String trade_type;
    private String currency_mark;
    private String human_date;
    private String title;
    private String description;
    private String profit;
    private String mark;

    private List<Inner> signalss;
   // private List<Currency> currenies;

    public Outer(String trade_type,String currency_mark, String human_date , String title , String description,String mark,String profit, List<Inner> signalss) {
        this.trade_type = trade_type;
        this.currency_mark = currency_mark;
        this.human_date = human_date;
        this.title = title;
        this.description = description;
        this.mark = mark;
        this.signalss = signalss;
        this.profit= profit;



    }

    public String getTrade_type() {
        return trade_type;
    }
    public String setTrade_type(String trade_type) {
        this.trade_type = trade_type;
        return trade_type;
    }

    public String getHuman_date() {
        return human_date;
    }
    public String setHuman_date(String human_date) {
        this.human_date = human_date;
        return human_date;
    }

    public String getTitle() {
        return title;
    }
    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getDescription() {
        return description;
    }
    public String setDescription(String description) {
        this.description = description;
        return description;
    }

    public String getCurrency_mark() {
        return currency_mark;
    }
    public String setCurrency_mark(String currency_mark) {
        this.currency_mark = currency_mark;
        return currency_mark;
    }




    public String getMark() {
        return mark;
    }
    public String setMark(String mark) {
        this.mark = mark;
        return mark;
    }


    public String getProfit() {
        return profit;
    }
    public String setProfit(String profit) {
        this.profit = profit;
        return profit;
    }


    public List<Inner> getSignalss() {
        return signalss;
    }
    public void setSignalss(List<Inner> signalss) {
        this.signalss = signalss;
    }
}
