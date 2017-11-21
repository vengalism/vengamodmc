/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;


import cofh.redstoneflux.api.IEnergyReceiver;
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
public class TileEntityEnergyBase extends TileEntityBase implements cofh.redstoneflux.api.IEnergyStorage{

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

    private void exchangeRF(IEnergyStorage from, IEnergyReceiver to, int energy){
        int extractedAmount = from.extractEnergy(energy, true);
        if(extractedAmount > 0){
            int filled = to.receiveEnergy(EnumFacing.NORTH, extractedAmount, false);
            from.extractEnergy(filled, false);
        }
    }


    public void extractToAdjacentRF(){
        for(IEnergyReceiver ier : getRFRecTilesAdjacent()){
            if(ier != null){
                exchangeRF(this.storage, ier, this.storage.getMaxCanExtract());
            }
        }
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
            }
        }
    }

    public void receiveFromAdjacent(){
        for(IEnergyStorage ies : getIESTilesAdjacent()){
            if(ies.canExtract()){
                exchangeEnergy(ies, this.storage, this.storage.getMaxCanReceive());
            }
        }
    }

    public void extractToItem(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage itemsStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            exchangeEnergy(this.storage, itemsStorage, this.storage.getMaxCanExtract());
        }
    }

    public void receiveFromItem(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage itemsStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            exchangeEnergy(itemsStorage, this.storage, this.storage.getMaxCanReceive());
        }
    }

    private ArrayList<IEnergyReceiver> getRFRecTilesAdjacent(){
        ArrayList<IEnergyReceiver> adjacentIER = new ArrayList<>();
        for(TileEntity tileEntity : MyUtil.getAdjacentBlocks(this.world, this.pos)){
            if(tileEntity != null) {
                if(tileEntity instanceof IEnergyReceiver){
                    IEnergyReceiver iEnergyReceiver = (IEnergyReceiver)tileEntity;
                    adjacentIER.add(iEnergyReceiver);
                }
            }
        }
        return adjacentIER;

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
    public CustomForgeEnergyStorage getEnergyStorage() {
        return this.storage;
    }


    //RF stuff
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.storage.getMaxEnergyStored();
    }
}
