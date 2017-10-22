/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomForgeEnergyStorage extends EnergyStorage{
    public boolean justMade = true;

    public CustomForgeEnergyStorage(int capacity) {
        this(capacity, 1000);
    }

    public CustomForgeEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public CustomForgeEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public CustomForgeEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        if(this.justMade){
            if(energy > 0) {
                this.setEnergy(energy);
                this.energy = energy;
            }
            this.justMade = false;
        }

    }

    public int getMaxCanExtract() {
        return this.getEnergyStored() < this.maxExtract ? this.getEnergyStored() : this.maxExtract;
    }

    public int getMaxCanReceive() {
        return (this.getEnergyStored() + this.maxReceive) > this.getMaxEnergyStored() ?
                this.getMaxEnergyStored() - this.getEnergyStored() : this.maxReceive;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(!this.canReceive()){
            return 0; // cant receive
        }

        //the amount it can take
        int receiveAmount = Math.min(this.getMaxCanReceive(), maxReceive);
        if(!simulate) {
            //set to new value
            if(this.getEnergyStored() + receiveAmount < this.getMaxEnergyStored()){
                this.setEnergy(this.getEnergyStored() + receiveAmount);

            }else{
                int difference = this.getMaxEnergyStored() - this.getEnergyStored();
                this.setEnergy(this.getMaxEnergyStored());
                return difference;
            }

        }
        //return amount received to be extracted from source
        return receiveAmount;

    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(!this.canExtract()){
            return 0; // cant extract
        }

        //the amount we can extract
        int extractAmount = Math.min(this.getMaxCanExtract(), maxExtract);
        if(!simulate) {
            //set to new value
            if(this.getEnergyStored() - extractAmount > 0){
                this.setEnergy(this.getEnergyStored() - extractAmount);
            }else{
                int remaining = this.getEnergyStored();
                this.setEnergy(0);
                return remaining;
            }

        }
        //return the amount we extracted
        return extractAmount;
    }

    public int receiveInternalEnergy(int maxReceive, boolean simulate){
        int before = this.maxReceive;
        this.maxReceive = Integer.MAX_VALUE;
        int returnValue = this.receiveEnergy(maxReceive, simulate);
        this.maxReceive = before;
        return returnValue;
    }

    public int extractInternalEnergy(int maxExtract, boolean simulate) {
        if(!this.canExtract()){
            return 0;
        }

        int returnValue = Math.min(this.getEnergyStored(), maxExtract);
        this.setEnergy(this.getEnergyStored() - returnValue);
        return returnValue;
    }

    int receiveInternalEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage) storage;
                return forgeEnergyStorage.receiveInternalEnergy(maxReceive, false);
            }
        }
        return 0;
    }

    public int extractInternalEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage) storage;
                return forgeEnergyStorage.extractInternalEnergy(maxExtract, false);
            }
        }
        return 0;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.justMade = nbt.getBoolean("justMadEe");
        if(!this.justMade){
            this.setEnergy(nbt.getInteger("myEnergy"));
        }
        //this.setEnergy(nbt.getInteger("myEnergy"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("myEnergy", this.getEnergyStored());
        nbt.setBoolean("justMadEe", this.justMade);
        return nbt;
    }

}
