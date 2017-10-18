/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.proxy;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy implements prox {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(VengaModMc.instance, new GuiHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void addRenderRegister(ItemStack stack, ResourceLocation location, String variant) {

    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
