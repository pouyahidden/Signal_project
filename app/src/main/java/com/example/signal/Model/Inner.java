package com.example.signal.Model;



public class Inner {

    private String name;
    private String vall;

    public Inner(String name, String vall) {

        this.name = name;
        this.vall = vall;
    }


    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }


    public String getVall() {
        return vall;
    }

    public String setVall(String vall) {
        this.vall = vall;
        return vall;
    }
}
