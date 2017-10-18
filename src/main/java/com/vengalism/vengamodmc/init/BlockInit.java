/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.init;

import com.vengalism.vengamodmc.objects.blocks.BlockEnergyFurnace;
import com.vengalism.vengamodmc.objects.blocks.BlockEnergyGenerator;
import com.vengalism.vengamodmc.objects.blocks.BlockEnergyStorage;

public class BlockInit {

    public static final BlockEnergyStorage block_energy_storage = new BlockEnergyStorage("block_energy_storage");
    public static final BlockEnergyFurnace block_energy_furnace = new BlockEnergyFurnace("block_energy_furnace", false);
    public static final BlockEnergyGenerator block_energy_generator = new BlockEnergyGenerator("block_energy_generator");

    public static void init(){
        //block_energy_vault
    }

}
