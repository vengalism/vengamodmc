/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.fluid;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class CustomFluidTank extends FluidTank {

    public CustomFluidTank(int capacity) {
        super(capacity);
    }
    public CustomFluidTank(int capacity, boolean startMax) {
        super(capacity);

        if(startMax){
            this.setFluid(new FluidStack(FluidRegistry.WATER, capacity));
        }
    }

    public CustomFluidTank(@Nullable FluidStack fluidStack, int capacity){
        super(fluidStack, capacity);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        if(fluid != null) {
            if (fluid.getFluid() == FluidRegistry.WATER) {

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canFill() {
        return super.canFill();
    }
}
