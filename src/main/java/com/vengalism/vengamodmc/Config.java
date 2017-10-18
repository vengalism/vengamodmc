/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc;

import com.vengalism.vengamodmc.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

//TODO this for config
/*
allow for different values in the generation of power for the blocks, storage amount etc
 */
/**
 * Created by vengada at 16/10/2017
 */
public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_ENERGY = "energy";

    // This values below you can access elsewhere in your mod:
    public static boolean isThisAGoodTutorial = true;
    public static String yourRealName = "Steve";
    public static int energyGeneratorEnergyPerTick = 50;

    public static void readConfig(){
        Configuration cfg = CommonProxy.config;
        try{
            cfg.load();
            initEnergyConfig(cfg);
        }catch (Exception e){
            System.out.println("Problem loading config files");
        }finally {
            if(cfg.hasChanged()){
                cfg.save();
            }
        }
    }

    private static void initEnergyConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        isThisAGoodTutorial = cfg.getBoolean("goodTutorial", CATEGORY_GENERAL, isThisAGoodTutorial, "Set to false if you don't like this tutorial");
        yourRealName = cfg.getString("realName", CATEGORY_GENERAL, yourRealName, "Set your real name here");
        energyGeneratorEnergyPerTick = cfg.getInt("energyGenPerTick", CATEGORY_ENERGY, energyGeneratorEnergyPerTick, 1, 500, "Amount of Energy the Energy Generator makes per 20 Ticks");
    }

}
