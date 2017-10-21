package com.vengalism.vengamodmc.objects.fluid;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

/**
 * Created by vengada at 20/10/2017
 */
public class LiquidNutrient extends MaterialLiquid{

    public LiquidNutrient(MapColor color) {
        super(color);
        this.setNoPushMobility();
    }

    @Override
    public boolean isLiquid() {
        return true;
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
