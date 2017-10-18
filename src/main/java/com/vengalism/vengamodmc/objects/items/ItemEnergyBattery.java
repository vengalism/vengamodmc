/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemEnergyBattery extends ItemEnergy {


    public ItemEnergyBattery(String name, int capacity, int maxReceive, int maxExtract, int energy) {
        super(name, capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        System.out.println(this.getEnergyStored(playerIn.getHeldItem(handIn)));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public int getItemStackLimit() {
        return 1;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return super.showDurabilityBar(stack);
    }
}
