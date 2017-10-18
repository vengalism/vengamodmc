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
public class ItemHydroAirStone extends ItemBase {
    private int lifespan;
    private int upkeepCost;

    public ItemHydroAirStone(String name, int lifespan, int upkeepCost) {
        super(name);
        this.lifespan = lifespan;
        this.upkeepCost = upkeepCost;
        this.setMaxStackSize(1);
    }

    public int getLifespan() {
        return this.lifespan;
    }

    //repair/clean
    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Lifespan: " + this.lifespan);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public void doUpkeep(){
        this.lifespan -= upkeepCost;
    }
}
