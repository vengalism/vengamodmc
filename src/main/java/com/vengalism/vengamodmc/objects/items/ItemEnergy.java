/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.capabilities.EnergyCapabilityProvider;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemEnergy extends ItemBase {

    public int capacity;
    public int maxExtract, maxReceive;
    public int energy;

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
        this.energy = energy;
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

    private IEnergyStorage getIStorage(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            return itemStack.getCapability(CapabilityEnergy.ENERGY, null);
        }
        return null;
    }

    private CustomForgeEnergyStorage getCustStorage(IEnergyStorage storage){
        if(storage != null) {
            if (storage instanceof CustomForgeEnergyStorage) {
                return (CustomForgeEnergyStorage) storage;
            }
        }
        return new CustomForgeEnergyStorage(0, 0,0);
    }

    public void setEnergy(ItemStack itemStack, int energy){
        getCustStorage(getIStorage(itemStack)).setEnergy(energy);
    }

    public int extractInternalEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        return getCustStorage(getIStorage(itemStack)).extractInternalEnergy(maxExtract, simulate);
    }

    public int getEnergyStored(ItemStack itemStack){
        return getCustStorage(getIStorage(itemStack)).getEnergyStored();
    }

    private int getMaxEnergyStored(ItemStack itemStack) {
        return getCustStorage(getIStorage(itemStack)).getMaxEnergyStored();

    }
}
