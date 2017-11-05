/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.init;

import com.vengalism.vengamodmc.objects.blocks.*;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.tileentities.TileEntityHome;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockInit {

    public static final BlockEnergyStorage block_energy_storage = new BlockEnergyStorage("block_energy_storage"); //tier 1
    public static final BlockEnergyStorage block_energy_storage_tier1 = new BlockEnergyStorage("block_energy_storage_tier_1", Enums.MACHINETIER.ONE);
    public static final BlockEnergyStorage block_energy_storage_tier2 = new BlockEnergyStorage("block_energy_storage_tier_2", Enums.MACHINETIER.TWO);
    public static final BlockEnergyStorage block_energy_storage_tier3 = new BlockEnergyStorage("block_energy_storage_tier_3", Enums.MACHINETIER.THREE);
    public static final BlockEnergyFurnace block_energy_furnace = new BlockEnergyFurnace("block_energy_furnace", false);
    public static final BlockEnergyFurnace block_energy_furnace_tier_1 = new BlockEnergyFurnace("block_energy_furnace_tier_1", false, Enums.MACHINETIER.ONE);
    public static final BlockEnergyFurnace block_energy_furnace_tier_2 = new BlockEnergyFurnace("block_energy_furnace_tier_2", false, Enums.MACHINETIER.TWO);
    public static final BlockEnergyFurnace block_energy_furnace_tier_3 = new BlockEnergyFurnace("block_energy_furnace_tier_3", false, Enums.MACHINETIER.THREE);
    public static final BlockEnergyGenerator block_energy_generator = new BlockEnergyGenerator("block_energy_generator");
    public static final BlockEnergyGenerator block_energy_generator_tier_1 = new BlockEnergyGenerator("block_energy_generator_tier_1", Enums.MACHINETIER.ONE);
    public static final BlockEnergyGenerator block_energy_generator_tier_2 = new BlockEnergyGenerator("block_energy_generator_tier_2", Enums.MACHINETIER.TWO);
    public static final BlockEnergyGenerator block_energy_generator_tier_3 = new BlockEnergyGenerator("block_energy_generator_tier_3", Enums.MACHINETIER.THREE);
    public static final BlockHydroTank block_hydro_nutrient_tank = new BlockHydroTank("block_hydro_nutrient_tank");
    public static final BlockHydroFishTank block_hydro_fish_tank = new BlockHydroFishTank("block_hydro_fish_tank");
    public static final BlockHydroCropTub block_hydro_crop_tub = new BlockHydroCropTub("block_hydro_crop_tub");
    //public static final BlockHome block_home = new BlockHome("block_home");




    public static void init(){

    }



}
