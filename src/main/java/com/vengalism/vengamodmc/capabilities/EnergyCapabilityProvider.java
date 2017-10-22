/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.capabilities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.objects.items.ItemEnergy;
import com.vengalism.vengamodmc.objects.items.ItemHydroAirStone;
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
                if(!justMade) {
                    itemStack.getTagCompound().setInteger("Energy", energy);
                }
            }
        };
    }

    public EnergyCapabilityProvider(ItemStack itemStack, ItemEnergy itemEnergy){
        int startMax = 0;
        if(itemEnergy instanceof ItemHydroAirStone){
            startMax = 1000;
        }
        this.storage = new CustomForgeEnergyStorage(itemEnergy.capacity, itemEnergy.maxReceive, itemEnergy.maxExtract, startMax) {
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
                    justMade = false;
                }
                if(!justMade) {
                    itemStack.getTagCompound().setInteger("Energy", energy);
                }
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
