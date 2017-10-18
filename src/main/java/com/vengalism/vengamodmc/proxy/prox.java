/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.proxy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface prox {

    void preInit(FMLPreInitializationEvent event);

    void init(FMLInitializationEvent event);

    void postInit(FMLPostInitializationEvent event);

    void addRenderRegister(ItemStack stack, ResourceLocation location, String variant);

    void registerItemRenderer(Item item, int meta, String id);

}
