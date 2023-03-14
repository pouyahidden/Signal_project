package com.example.signal.Model;

public class ChatItemModel {

    // Integers assigned to each layout
    // these are declared static so that they can
    // be accessed from the class name itself
    // And final so that they are not modified later
    public static final int User_chat = 0;
    public static final int System_chat = 1;

    // This variable ViewType specifies
    // which out of the two layouts
    // is expected in the given item
    private int viewType;

    // String variable to hold the TextView
    // of the first item.
    private int icon1;
    private String title1, description1,time1,dd;

    // public constructor for the first layout
    public ChatItemModel(int viewType, int icon1, String title1, String description1,String time1,String dd) {
        this.icon1 = icon1;
        this.title1 = title1;
        this.description1 = description1;
        this.time1 = time1;
        this.dd= dd;
        this.viewType = viewType;
    }

    // getter and setter methods for the text variable
    public int getIcon1() { return icon1; }

    public void setIcon1(int icon1) { this.icon1 = icon1; }

    public String getTitle1() { return title1; }

    public void setTitle1(String title1)
    {
        this.title1 = title1;
    }

    public String getDescription1() { return description1; }

    public void setDescription1(String description1)
    {
        this.description1 = description1;
    }

    public String getTime1() { return time1; }

    public void setTime1(String time1)
    {
        this.time1 = time1;
    }

    public String getDd() { return dd; }

    public void setDd(String dd)
    {
        this.time1 = dd;
    }

    // Variables for the item of second layout
    private int icon;
    private String title, description,time;

    // public constructor for the second layout
    public ChatItemModel(int viewType, int icon, String title, String description,String time)
    {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.time = time;
        this.viewType = viewType;
    }

    // getter and setter methods for
    // the variables of the second layout

    public int geticon() { return icon; }

    public void seticon(int icon) { this.icon = icon; }

    public String getTitle() { return title; }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription() { return description; }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTime() { return time; }

    public void setTime(String time)
    {
        this.time = time;
    }


    public int getViewType() { return viewType; }

    public void setViewType(int viewType)
    {
        this.viewType = viewType;
    }
}