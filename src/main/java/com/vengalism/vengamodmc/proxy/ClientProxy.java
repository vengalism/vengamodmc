/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.proxy;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public class ClientProxy implements prox {

    public static Configuration config;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "vengamodmc.cfg"));
        Config.readConfig();
        System.out.println("JUST READ CONFIG");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(VengaModMc.instance, new GuiHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if(config.hasChanged()){
            System.out.println("CONFIG CHANGED SAVING");
            config.save();
        }
    }

    @Override
    public void addRenderRegister(ItemStack stack, ResourceLocation location, String variant) {

    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
