/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc;

import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.proxy.prox;
import com.vengalism.vengamodmc.tabs.VengamodmcTab;
import com.vengalism.vengamodmc.tileentities.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
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

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());
        PacketHandler.init();
        FluidInit.init();
        FluidRegistry.addBucketForFluid(FluidInit.fluid_nutrient);
        FluidRegistry.addBucketForFluid(FluidInit.fluid_nutrient_oxygenated);

        proxy.preInit(event);


    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){
        GuiHandler.init();
        GameRegistry.registerTileEntity(TileEntityEnergyStorage.class, Reference.MODID + "TileEntityEnergyStorage");
        GameRegistry.registerTileEntity(TileEntityEnergyBase.class, Reference.MODID + "TileEntityEnergyBase");
        GameRegistry.registerTileEntity(TileEntityEnergyFurnace.class, Reference.MODID + "TileEntityEnergyFurnace");
        GameRegistry.registerTileEntity(TileEntityEnergyGenerator.class, Reference.MODID + "TileEntityEnergyGenerator");
        GameRegistry.registerTileEntity(TileEntityHydroNutrientTank.class, Reference.MODID + "TileEntityHydroNutrientTank");
        GameRegistry.registerTileEntity(TileEntityHydroFishTank.class, Reference.MODID + "TileEntityHydroFishTank");
        GameRegistry.registerTileEntity(TileEntityHydroCropTub.class, Reference.MODID + "TileEntityHydroCropTub");
        GameRegistry.registerTileEntity(TileEntityHome.class, Reference.MODID + "TileEntityHome");
        proxy.init(event);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
        //System.out.println("PROXY POSTING");
    }
}
