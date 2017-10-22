/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemHydroAirStone extends ItemEnergy{
    private int upkeepCost;

    public ItemHydroAirStone(String name, int lifespan, int upkeepCost) {
        super(name, lifespan, 0, 1, lifespan);
        this.upkeepCost = upkeepCost;
        this.setMaxStackSize(1);
        this.setEnergy(new ItemStack(this), lifespan);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }




}
