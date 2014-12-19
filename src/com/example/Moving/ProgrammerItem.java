package com.example.Moving;

/**
 * Created by tiny on 12/19/14.
 */
public class ProgrammerItem {
    private String name;
    private int personImage;

    public ProgrammerItem(String name,int personImage){
        this.name=name;
        this.personImage = personImage;
    }

    public String getName(){
        return name;
    }

    public int getPersonImage(){
        return personImage;
    }

}
