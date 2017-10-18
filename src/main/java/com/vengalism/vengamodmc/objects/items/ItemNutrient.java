/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.hydro.INutrient;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemNutrient extends ItemBase implements INutrient {

    private String name;
    private int worth;

    public ItemNutrient(String name){
        this(name, 50);
    }

    public ItemNutrient(String name, int worth){
        super(name);
        this.name = name;
        this.worth = worth;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String toString(){
        return "Name : " + getName() + " Worth : " + getWorth();
    }

    @Override
    public int getWorth() {
        return this.worth;
    }

}
