package com.example.Moving;

/**
 * Created by tiny on 12/18/14.
 */
public class MainListItem {
    private String name;
    private int itemImageId;
    private int itemToId;

    public MainListItem(String name,int itemImageId,int itemToId){
        this.name=name;
        this.itemImageId=itemImageId;
        this.itemToId=itemToId;
    }

    public String getName(){
        return name;
    }

    public int getItemImageId(){
        return itemImageId;
    }

    public int getItemToId(){
        return itemToId;
    }

    public void changeItemTo(int itemToId){
        this.itemToId=itemToId;
    }
}
