/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.util.MyUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by vengada at 15/10/2017
 */
public class TileEntityEnergyBase extends TileEntityBase {

    protected CustomForgeEnergyStorage storage;


    public TileEntityEnergyBase( int capacity) {
        this(capacity, 1000);
    }

    public TileEntityEnergyBase(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public TileEntityEnergyBase(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public TileEntityEnergyBase(int capacity, int maxReceive, int maxExtract, int energy) {
        this.storage = new CustomForgeEnergyStorage(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY){
            return (T) this.storage;
        }
        return super.getCapability(capability, facing);
    }

    private void exchangeEnergy(IEnergyStorage from, IEnergyStorage to, int energy){
        int extractedAmount = from.extractEnergy(energy, true);
        if(extractedAmount > 0){
            if(to.canReceive()) {
                int filled = to.receiveEnergy(extractedAmount, false);
                from.extractEnergy(filled, false);
            }
        }
    }

    //extract from me, receive to other
    public void extractToAdjacent(){
        for(IEnergyStorage ies : getIESTilesAdjacent()){
            if(ies.canReceive()){
                exchangeEnergy(this.storage, ies, this.storage.getMaxCanExtract());
                /*
                int availFromStorage = this.storage.extractEnergy(this.storage.getMaxCanExtract(), true);
                int leftover = ies.receiveEnergy(availFromStorage, false);
                this.storage.extractEnergy(leftover, false);
                */
            }
        }
    }

    public void receiveFromAdjacent(){
        for(IEnergyStorage ies : getIESTilesAdjacent()){
            if(ies.canExtract()){
                exchangeEnergy(ies, this.storage, this.storage.getMaxCanReceive());
                /*
                int availFromIES = ies.extractEnergy(this.storage.getMaxCanReceive(), true);
                int leftover = this.storage.receiveEnergy(availFromIES, false);
                ies.extractEnergy(leftover, false);
                */
            }
        }
    }

    public void extractToItem(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage) storage;
                forgeEnergyStorage.extractInternalEnergy(this.storage.getMaxCanExtract(), false);
            }
        }
    }

    public void receiveFromItem(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if(storage instanceof CustomForgeEnergyStorage){
                CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage) storage;
                forgeEnergyStorage.receiveInternalEnergy(this.storage.getMaxCanReceive(), false);
            }
        }
    }

    private ArrayList<IEnergyStorage> getIESTilesAdjacent(){
        ArrayList<IEnergyStorage> adjacentIES = new ArrayList<>();
        for(TileEntity tileEntity : MyUtil.getAdjacentBlocks(this.world, this.pos)){
            if(tileEntity != null) {
                if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, null)) {
                    IEnergyStorage ies = tileEntity.getCapability(CapabilityEnergy.ENERGY, null);
                    adjacentIES.add(ies);
                }
            }
        }
        return adjacentIES;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.storage.setEnergy(compound.getInteger("Energy"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Energy", this.storage.getEnergyStored());
        return compound;
    }

}
