/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.fluid;

import net.minecraftforge.fluids.Fluid;
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
            if (fluid == this.getFluid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canFill() {
        return super.canFill();
    }

    //water generally means an error
    public FluidStack getMaxCanFill(){
        int amt = this.getFluidAmount() + Fluid.BUCKET_VOLUME > this.capacity ?
                this.getCapacity() - this.getFluidAmount() : Fluid.BUCKET_VOLUME;
        return this.fluid != null ? new FluidStack(this.fluid, amt) : new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
    }
}
