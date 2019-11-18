package com.example.bankingapp;

public class RecyclerGridItems {
    private int imageResource;
    private String action;

    public RecyclerGridItems(int imageResource,String action)
    {
        this.imageResource=imageResource;
        this.action=action;

    }

    public int getImageResource() {
        return imageResource;
    }

    public String getAction() {
        return action;
    }
}
