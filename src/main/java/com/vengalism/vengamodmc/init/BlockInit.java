/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.init;

import com.vengalism.vengamodmc.objects.blocks.*;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.tileentities.TileEntityHome;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockInit {

    public static final BlockEnergyStorage block_energy_storage = new BlockEnergyStorage("block_energy_storage");
    public static final BlockEnergyFurnace block_energy_furnace = new BlockEnergyFurnace("block_energy_furnace", false);
    public static final BlockEnergyGenerator block_energy_generator = new BlockEnergyGenerator("block_energy_generator");
    public static final BlockHydroTank block_hydro_nutrient_tank = new BlockHydroTank("block_hydro_nutrient_tank");
    public static final BlockHydroFishTank block_hydro_fish_tank = new BlockHydroFishTank("block_hydro_fish_tank");
    public static final BlockHydroCropTub block_hydro_crop_tub = new BlockHydroCropTub("block_hydro_crop_tub");
    //public static final BlockHome block_home = new BlockHome("block_home");




    public static void init(){
        //block_energy_vault
    }



}
