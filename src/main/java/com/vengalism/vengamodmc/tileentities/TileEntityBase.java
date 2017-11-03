/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by vengada at 15/10/2017
 */
public class TileEntityBase extends TileEntity{

    public int sync = 0;

    public boolean hasInvHandler(){
        return getInvHandler() != null;
    }

    public ItemStackHandler getInvHandler(){
        return null;
    }

    public boolean hasFluidTank(){
        return getFluidTank() != null;
    }

    public CustomFluidTank getFluidTank(){
        return null;
    }

    public boolean hasEnergyStorage(){
        return getEnergyStorage() != null;
    }

    public CustomForgeEnergyStorage getEnergyStorage(){
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(hasInvHandler()){
            this.getInvHandler().deserializeNBT(compound.getCompoundTag("invHandler"));
        }

        if(hasFluidTank()){
            this.getFluidTank().readFromNBT(compound);
        }

        if(hasEnergyStorage()){
            this.getEnergyStorage().readFromNBT(compound);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if(hasInvHandler()){
            compound.setTag("invHandler", getInvHandler().serializeNBT());
        }

        if(hasFluidTank()){
            this.getFluidTank().writeToNBT(compound);
        }

        if(hasEnergyStorage()){
            this.getEnergyStorage().writeToNBT(compound);
        }

        return compound;
    }
}
