/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.proxy;

import com.vengalism.vengamodmc.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy implements prox{


    public static Configuration config;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "vengamodmc.cfg"));
        Config.readConfig();
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if(config.hasChanged()){
            config.save();
        }
    }

    @Override
    public void addRenderRegister(ItemStack stack, ResourceLocation location, String variant) {

    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {

    }
}