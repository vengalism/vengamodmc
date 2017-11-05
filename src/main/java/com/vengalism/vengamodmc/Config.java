/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc;

import com.vengalism.vengamodmc.proxy.ClientProxy;
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

    private static final String CATEGORY_GENERAL = "General";
    private static final String CATEGORY_ENERGY = "Energy";
    private static final String CATEGORY_HYDRO = "Hydro";
    private static final String CATEGORY_ITEM = "Items";

    // This values below you can access elsewhere in your mod:
    //public static boolean isThisAGoodTutorial = true;
    //public static String yourRealName = "Steve";
    public static int tierOneMultiplier = 1, tierTwoMultiplier = 2, tierThreeMultiplier = 3;
    public static int energyGeneratorEnergyPerTick = 50, energyGeneratorMaxEnergyStored = 20000, energyGeneratorEnergyExtractSpeed = 500;
    public static int energyFurnaceCookSpeed = 10, energyFurnaceMaxEnergyStored = 10000, energyFurnaceEnergyReceiveSpeed = 500, energyFurnaceSmeltUpkeep = 5;
    public static boolean energyFurnaceAutoExport = false;
    public static int energyStorageMaxeEnergyStored = 50000, energyStorageMaxExtractSpeed = 1000, energyStorageMaxeReceive = 1000;
    public static int hydroFishTankMaxTime = 1000, hydroFishTankFluidGen = 50;
    public static int hydroCropTubMaxDelay = 5000, hydroCropTubFluidUpkeep = 2, hydroCropTubHarvestUpkeep = 6;
    public static int fluidNutrientDelay = 2700, fluidOxNutrientDelay = 3400;
    public static boolean hydroCropTubAutoHarvest = true;

    public static int hydroNutrientTankFluidGen = 50;

    public static int itemNutrientMixtureUpkeepCost = 1;
    public static int itemHydroAirStoneUpkeepCost = 1;
    public static int itemEnergyBatteryCapacity = 10000, itemEnergyBatteryMaxReceive = 500, itemEnergyBatteryMaxExtract = 500;

    public static int axeEnergyPerUse = 100, hoeEnergyPerUse = 100, pickaxeEnergyPerUse = 100, shovelEnergyPerUse = 100, swordEnergyPerUse = 100;
    public static int toolBaseCapacity = 10000, toolBaseMaxExtract = 100, toolBaseMaxReceive = 1000;

    public static void readConfig(){
        Configuration cfg = ClientProxy.config;
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
        //isThisAGoodTutorial = cfg.getBoolean("goodTutorial", CATEGORY_GENERAL, isThisAGoodTutorial, "Set to false if you don't like this tutorial");
        //yourRealName = cfg.getString("realName", CATEGORY_GENERAL, yourRealName, "Set your real name here");
        tierOneMultiplier = cfg.getInt("Tier 1 Multiplier", CATEGORY_GENERAL, tierOneMultiplier, 1, 10, "extra bonus for type for tier 1 machines. i.e. base 100000 * 1(multiplier) = 10000");
        tierTwoMultiplier = cfg.getInt("Tier 2 Multiplier", CATEGORY_GENERAL, tierTwoMultiplier, 1, 10, "extra bonus for type for tier 2 machines. i.e. base 100000 * 2(multiplier) = 20000");
        tierThreeMultiplier = cfg.getInt("Tier 3 Multiplier", CATEGORY_GENERAL, tierThreeMultiplier, 1, 10, "extra bonus for type for tier 3 machines. i.e. base 100000 * 3(multiplier) = 30000");

        cfg.addCustomCategoryComment(CATEGORY_ENERGY, "Energy Configuration");
        energyGeneratorEnergyPerTick = cfg.getInt("Energy Generator: Energy Per 20 Ticks", CATEGORY_ENERGY, energyGeneratorEnergyPerTick, 1, 500, "Amount of Energy the Energy Generator makes per 20 Ticks BEFORE tier multiplier");
        energyGeneratorMaxEnergyStored = cfg.getInt("Energy Generator: Capacity", CATEGORY_ENERGY, energyGeneratorMaxEnergyStored, 1000, 100000, "Amount of Energy the Energy Generator can store BEFORE tier multiplier");
        energyGeneratorEnergyExtractSpeed = cfg.getInt("Energy Generator: Max Extract", CATEGORY_ENERGY, energyGeneratorEnergyExtractSpeed, 1, 5000, "Amount of Energy the Energy Generator extracts BEFORE tier multiplier");

        energyFurnaceCookSpeed = cfg.getInt("Energy Furnace: Cook Speed", CATEGORY_ENERGY, energyFurnaceCookSpeed, 2, 1000, "Cook speed of the furnace, bigger is slower");
        energyFurnaceMaxEnergyStored = cfg.getInt("Energy Furnace: Capacity", CATEGORY_ENERGY, energyFurnaceMaxEnergyStored, 1000, 100000, "Amount of Energy the Energy furnace can store BEFORE tier multiplier");
        energyFurnaceEnergyReceiveSpeed = cfg.getInt("Energy Furnace: Max Receive", CATEGORY_ENERGY, energyFurnaceEnergyReceiveSpeed, 1, 5000, "Amount of Energy the Energy furnace receives BEFORE tier multiplier");
        energyFurnaceSmeltUpkeep = cfg.getInt("Energy Furnace: Smelt Upkeep", CATEGORY_ENERGY, energyFurnaceSmeltUpkeep, 1, 1000, "Amount to drain when successfully smelts an item");
        energyFurnaceAutoExport = cfg.getBoolean("Energy Furnace: Auto Export", CATEGORY_ENERGY, energyFurnaceAutoExport, "Enable/Disable auto export of items from the furnace. Note, not quite working as intended yet");

        energyStorageMaxeEnergyStored = cfg.getInt("Energy Storage: Capacity", CATEGORY_ENERGY, energyStorageMaxeEnergyStored, 1000, 1000000, "Amount of Energy the Energy storage Stores before multiplier from type");
        energyStorageMaxExtractSpeed = cfg.getInt("Energy Storage: Max Extract", CATEGORY_ENERGY, energyStorageMaxExtractSpeed, 1, 5000, "Amount of Energy the Energy storage extracts before multiplier from type");
        energyStorageMaxeReceive = cfg.getInt("Energy Storage: Max Receive", CATEGORY_ENERGY, energyStorageMaxeReceive, 1, 5000, "Amount of Energy the Energy storage receives before multiplier from type");



        cfg.addCustomCategoryComment(CATEGORY_HYDRO, "Hydroponic Configuration");
        hydroFishTankMaxTime = cfg.getInt("Hydro Fish Tank: MaxTime", CATEGORY_HYDRO, hydroFishTankMaxTime, 10, 10000, "Duration to wait before fish tank breeds/kills fish");
        hydroFishTankFluidGen = cfg.getInt("Hydro Fish Tank: Fluid Generation", CATEGORY_HYDRO, hydroFishTankFluidGen, 1, 1000, "Duration to wait before fish tank breeds/kills fish");

        fluidNutrientDelay = cfg.getInt("Fluid: Nutrient Delay", CATEGORY_HYDRO, fluidNutrientDelay, 1, 1000, "the amount to reduce the delay by for faster update for having Nutrient Fluid, bigger is faster");
        fluidOxNutrientDelay = cfg.getInt("Fluid: Oxygenated Nutrient Delay", CATEGORY_HYDRO, fluidOxNutrientDelay, 1, 1000, "the amount to reduce the delay by for faster update for having Oxygenated Nutrient Fluid, bigger is faster");

        hydroCropTubMaxDelay = cfg.getInt("HydroCropTubMaxDelay", CATEGORY_HYDRO, hydroCropTubMaxDelay, 1, 1000, "Duration to wait before crop tub schedules an update to force crop to grow");
        hydroCropTubAutoHarvest = cfg.getBoolean("Hydro Crop Tub: Auto Harvest", CATEGORY_HYDRO, hydroCropTubAutoHarvest, "Enable/Disable auto harvesting of the crop above");
        hydroCropTubFluidUpkeep = cfg.getInt("Hydro Crop Tub: Fluid Upkeep", CATEGORY_HYDRO, hydroCropTubFluidUpkeep, 1, 500, "amount of fluid per 20 ticks to drain the tub of nutrient(feeding the plants)");
        hydroCropTubHarvestUpkeep = cfg.getInt("Hydro Crop Tub: Harvest Upkeep", CATEGORY_HYDRO, hydroCropTubHarvestUpkeep, 1, 500, "amount of fluid lost while harvesting the crop above");

        hydroNutrientTankFluidGen = cfg.getInt("Hydro Nutrient Tank: Fluid Generation", CATEGORY_HYDRO, hydroNutrientTankFluidGen, 1, 500, "amount of fluid generated through processing");

        cfg.addCustomCategoryComment(CATEGORY_ITEM, "Item Configuration");
        itemEnergyBatteryCapacity = cfg.getInt("Energy Battery: Capacity", CATEGORY_ITEM, itemEnergyBatteryCapacity, 100, 50000, "Capacity for the Energy Battery Item");
        itemEnergyBatteryMaxReceive = cfg.getInt("Energy Battery: Max Receive", CATEGORY_ITEM, itemEnergyBatteryMaxReceive, 1, 5000, "Max Receive for the Energy Battery Item");
        itemEnergyBatteryMaxExtract = cfg.getInt("Energy Battery: Max Extract", CATEGORY_ITEM, itemEnergyBatteryMaxExtract, 1, 5000, "Max Extract for the Energy Battery Item");

        itemHydroAirStoneUpkeepCost = cfg.getInt("Hydro Air Stone: Upkeep Cost", CATEGORY_ITEM, itemHydroAirStoneUpkeepCost, 1, 100, "upkeep cost for the hydro air stone, more drains the item faster");
        itemNutrientMixtureUpkeepCost = cfg.getInt("Nutrient Mixture: Upkeep Cost", CATEGORY_ITEM, itemNutrientMixtureUpkeepCost, 1, 100, "upkeep cost for the nutrient mixture, more drains the item faster");

        axeEnergyPerUse = cfg.getInt("Axe Energy Tool: Energy Per Use", CATEGORY_ITEM, axeEnergyPerUse, 1, 1000, "The amount of energy to drain per use");
        hoeEnergyPerUse = cfg.getInt("How Energy Tool: Energy Per Use", CATEGORY_ITEM, hoeEnergyPerUse, 1, 1000, "The amount of energy to drain per use");
        pickaxeEnergyPerUse = cfg.getInt("Pickaxe Energy Tool: Energy Per Use", CATEGORY_ITEM, pickaxeEnergyPerUse, 1, 1000, "The amount of energy to drain per use");
        shovelEnergyPerUse = cfg.getInt("Shovel Energy Tool: Energy Per Use", CATEGORY_ITEM, shovelEnergyPerUse, 1, 1000, "The amount of energy to drain per use");
        swordEnergyPerUse = cfg.getInt("Sword Energy Tool: Energy Per Use", CATEGORY_ITEM, swordEnergyPerUse, 1, 1000, "The amount of energy to drain per use");

        toolBaseCapacity = cfg.getInt("Tool Base: Capacity", CATEGORY_ITEM, toolBaseCapacity, 1000, 50000, "Base amount before tier multiplier");
        toolBaseMaxExtract = cfg.getInt("Tool Base: Max Extract", CATEGORY_ITEM, toolBaseMaxExtract, 1, 1000, "Base amount before tier multiplier");
        toolBaseMaxReceive = cfg.getInt("Tool Base: Max Receive", CATEGORY_ITEM, toolBaseMaxReceive, 1, 1000, "Base amount before tier multiplier");

    }

}
