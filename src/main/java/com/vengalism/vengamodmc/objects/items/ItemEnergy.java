/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.capabilities.EnergyCapabilityProvider;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemEnergy extends ItemBase{

    public int capacity;
    public int maxExtract, maxReceive;
    //public int energy;

    public ItemEnergy(String name, int capacity) {
        this(name, capacity, 1000);
    }

    public ItemEnergy(String name, int capacity, int maxTransfer) {
        this(name, capacity, maxTransfer, maxTransfer);
    }

    public ItemEnergy(String name, int capacity, int maxReceive, int maxExtract) {
        this(name, capacity, maxReceive, maxExtract, 0);
    }

    public ItemEnergy(String name, int capacity, int maxReceive, int maxExtract, int energy) {
        super(name);
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.setEnergy(new ItemStack(this), energy);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(String.format("Energy " + this.getEnergyStored(stack) + " / " + this.capacity));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new EnergyCapabilityProvider(stack, this);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if(stack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null) {
                double maxAmount = storage.getMaxEnergyStored();
                double energyDif = maxAmount - storage.getEnergyStored();
                return energyDif / maxAmount;
            }
        }
        return super.getDurabilityForDisplay(stack);
    }

    public void setEnergy(ItemStack itemStack, int energy){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                ((CustomForgeEnergyStorage)storage).setEnergy(energy);
            }
        }
    }


    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage != null){
                return storage.receiveEnergy(maxReceive, simulate);
            }
        }
        return 0;
    }

    public int extractEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage != null){
                return storage.extractEnergy(maxExtract, simulate);
            }
        }
        return 0;
    }

    public int extractInternalEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                return ((CustomForgeEnergyStorage)storage).extractInternalEnergy(maxExtract, simulate);
            }
        }
        return 0;
    }

    public int receiveInternalEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                return ((CustomForgeEnergyStorage)storage).receiveInternalEnergy(maxReceive, simulate);
            }
        }
        return 0;
    }

    public int getEnergyStored(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage != null){
                return storage.getEnergyStored();
            }
        }
        return 0;
    }

    public int getMaxEnergyStored(ItemStack itemStack) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage != null){
                return storage.getMaxEnergyStored();
            }
        }
        return 0;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        return true;
    }
}
