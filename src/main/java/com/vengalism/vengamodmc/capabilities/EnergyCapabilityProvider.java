/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.capabilities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.objects.items.ItemEnergy;
import com.vengalism.vengamodmc.objects.tools.ToolEnergy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyCapabilityProvider implements ICapabilityProvider {

    private CustomForgeEnergyStorage storage;

    public EnergyCapabilityProvider(ItemStack itemStack, ToolEnergy energyTool){
        this.storage = new CustomForgeEnergyStorage(energyTool.maxEnergyStored, energyTool.maxReceive, energyTool.maxExtract){
            @Override
            public int getEnergyStored(){
                if(itemStack.hasTagCompound()){
                    return itemStack.getTagCompound().getInteger("Energy");
                }
                else{
                    return 0;
                }
            }

            @Override
            public void setEnergy(int energy){
                if(!itemStack.hasTagCompound()){
                    itemStack.setTagCompound(new NBTTagCompound());
                }

                itemStack.getTagCompound().setInteger("Energy", energy);
            }
        };
    }

    public EnergyCapabilityProvider(ItemStack itemStack, ItemEnergy energyTool){

        this.storage = new CustomForgeEnergyStorage(energyTool.capacity, energyTool.maxReceive, energyTool.maxExtract, energyTool.energy) {
            @Override
            public int getEnergyStored() {
                if (itemStack.hasTagCompound()) {
                    return itemStack.getTagCompound().getInteger("Energy");
                } else {
                    return 0;
                }
            }

            @Override
            public void setEnergy(int energy) {
                if (!itemStack.hasTagCompound()) {
                    itemStack.setTagCompound(new NBTTagCompound());
                }
                itemStack.getTagCompound().setInteger("Energy", energy);
            }
        };
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY){
            return (T) this.storage;
        }
        return null;
    }
}
