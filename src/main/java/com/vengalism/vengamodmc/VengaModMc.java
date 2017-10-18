/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc;

import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import com.vengalism.vengamodmc.proxy.prox;
import com.vengalism.vengamodmc.tabs.VengamodmcTab;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyBase;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyFurnace;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyGenerator;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class VengaModMc {

    public static final CreativeTabs vengamodmctab = new VengamodmcTab("VengaModMc");

    @Mod.Instance(Reference.MODID)
    public static VengaModMc instance;

    @SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
    public static prox proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());
        PacketHandler.init();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){

        //RegistryHandler.BLOCKSTOREGISTER.clear();
        //RegistryHandler.ITEMSTOREGISTER.clear();
        GuiHandler.init();
        GameRegistry.registerTileEntity(TileEntityEnergyStorage.class, Reference.MODID + "TileEntityEnergyVault");
        GameRegistry.registerTileEntity(TileEntityEnergyBase.class, Reference.MODID + "TileEntityEnergyBase");
        GameRegistry.registerTileEntity(TileEntityEnergyFurnace.class, Reference.MODID + "TileEntityEnergyFurnace");
        GameRegistry.registerTileEntity(TileEntityEnergyGenerator.class, Reference.MODID + "TileEntityEnergyGenerator");
        proxy.init(event);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){

        proxy.postInit(event);
    }
}
