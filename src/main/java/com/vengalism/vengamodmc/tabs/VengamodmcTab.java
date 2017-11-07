/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tabs;

import com.vengalism.vengamodmc.init.BlockInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by vengada on 15/07/2017.
 */
public class VengamodmcTab extends CreativeTabs {

    public VengamodmcTab(String label) {

        super(label);
        this.setBackgroundImageName("vengamodmctab.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(BlockInit.block_energy_storage_tier1));
    }
}
