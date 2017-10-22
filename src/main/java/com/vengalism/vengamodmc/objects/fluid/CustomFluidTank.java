/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CustomFluidTank extends FluidTank {

    private ArrayList<Fluid> fluidTypes = new ArrayList<>();
    public boolean justMade = true;
    private int fluidMade = 0;
    public CustomFluidTank(int capacity) {
        super(capacity);
    }
    public CustomFluidTank(int capacity, boolean startMax) {
        super(capacity);

        if(justMade) {
            if (startMax) {
                this.setFluid(new FluidStack(FluidRegistry.WATER, capacity));

            }
            justMade = false;
        }


    }

    public CustomFluidTank(@Nullable FluidStack fluidStack, int capacity){
        super(fluidStack, capacity);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        if(fluidTypes.size() > 0){
            for(Fluid f : fluidTypes){
                if(fluid != null) {
                    if (fluid.getFluid() == f) {
                        return true;
                    }
                }
            }
        }else{
            fluidTypes.add(FluidRegistry.WATER);
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

    public ArrayList<Fluid> getFluidTypes() {
        return fluidTypes;
    }

    public void setFluidTypes(ArrayList<Fluid> fluidTypes) {
        this.fluidTypes = fluidTypes;
    }

    //NBT done in super by default seems ok


    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        this.justMade = nbt.getBoolean("justMade");
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("justMade", this.justMade);
        return super.writeToNBT(nbt);
    }
}
