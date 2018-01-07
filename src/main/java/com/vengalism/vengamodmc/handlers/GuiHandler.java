/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.handlers;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.client.gui.*;
import com.vengalism.vengamodmc.container.*;
import com.vengalism.vengamodmc.tileentities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Created by vengada on 16/07/2017.
 */
public class GuiHandler implements IGuiHandler {

    public static final int energyStorageContainerID  = 0, energyGeneratorContainerID = 1, energyFurnaceContainerID = 3,
        hydroTubContainerID = 5, hydroTankContainerID = 6, homeContainerID = 7, hydroFishTankContainerID = 8;

    public static final int diggerContainerID = 9, diggerControllerContainerID = 10;

    public static void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(VengaModMc.instance, new GuiHandler());
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case energyStorageContainerID:
                return new ContainerEnergyStorage(player.inventory, (TileEntityEnergyStorage) te);
            case energyGeneratorContainerID:
                return new ContainerEnergyGenerator(player.inventory, (TileEntityEnergyGenerator) te);
            case energyFurnaceContainerID:
                return new ContainerEnergyFurnace(player.inventory, (TileEntityEnergyFurnace) te);
            case hydroTankContainerID:
                return new ContainerHydroTank(player.inventory, (TileEntityHydroNutrientTank) te);
            case hydroTubContainerID:
                return new ContainerHydroCropTub(player.inventory, (TileEntityHydroCropTub) te);
            case hydroFishTankContainerID:
                return new ContainerHydroFishTank(player.inventory, (TileEntityHydroFishTank) te);
            case diggerContainerID:
                return new ContainerDigger(player.inventory, (TileEntityDigger)te);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case energyStorageContainerID:
                return new GUIEnergyStorage(player.inventory, (TileEntityEnergyStorage) te);
            case energyGeneratorContainerID:
                return new GUIEnergyGenerator(player.inventory, (TileEntityEnergyGenerator) te);
            case energyFurnaceContainerID:
                return new GUIEnergyFurnace(player.inventory, (TileEntityEnergyFurnace) te);
            case hydroTankContainerID:
                return new GUIHydroNutrientTank(player.inventory, (TileEntityHydroNutrientTank) te);
            case hydroTubContainerID:
                return new GUIHydroCropTub(player.inventory, (TileEntityHydroCropTub) te);
            case hydroFishTankContainerID:
                return new GUIHydroFishTank(player.inventory, (TileEntityHydroFishTank)te);
            case diggerContainerID:
                return new GUIDigger(player.inventory, (TileEntityDigger)te);
            default:
                return null;
        }
    }
}
