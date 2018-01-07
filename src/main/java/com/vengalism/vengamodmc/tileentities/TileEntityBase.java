/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import com.google.gson.JsonObject;


/**
 * Created by vengada at 15/10/2017
 */
public class TileEntityBase extends TileEntity {

    public int sync = 0;

    public boolean hasMachineTier(){
        return getMachineTier() != null;
    }

    public Enums.MACHINETIER getMachineTier(){
        return null;
    }

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

    public JsonObject getPacketData() {
        JsonObject data = new JsonObject();

        JsonObject blockInfo = new JsonObject();
        blockInfo.addProperty("name", this.getBlockType().getLocalizedName());
        if(hasMachineTier()){
            blockInfo.addProperty("machineTier", getMachineTier().getName());
        }
        data.add("blockInfo", blockInfo);


        if(hasEnergyStorage()){
            JsonObject energyStorage = new JsonObject();
            energyStorage.addProperty("energy", getEnergyStorage().getEnergyStored());
            energyStorage.addProperty("maxEnergy", getEnergyStorage().getMaxEnergyStored());
            data.add("energyStorage", energyStorage);
        }

        if(hasFluidTank()){
            JsonObject fluidStorage = new JsonObject();
            FluidStack fluid = getFluidTank().getFluid();
            if(fluid != null){
                fluidStorage.addProperty("fluidName", getFluidTank().getFluid().getLocalizedName());
                fluidStorage.addProperty("fluidAmount", getFluidTank().getFluidAmount());
                fluidStorage.addProperty("fluidMaxAmount", getFluidTank().getCapacity());
                data.add("fluidStorage", fluidStorage);
            }

        }
        data.addProperty("valid", true);

        return data;
    }
}
