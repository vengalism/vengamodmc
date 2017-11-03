/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada at 18/10/2017
 */
public class TileEntityCircuitAssembler extends TileEntityEnergyBase{

    private ItemStackHandler invHandler;

    public TileEntityCircuitAssembler(){
        super(10000, 500, 0);
        this.invHandler = new ItemStackHandler(10);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return (T) this.invHandler;}
        return super.getCapability(capability, facing);
    }

    //look for crafting potential
    private void checkGrid(){
        for(int i = 0; i < 9; i++){
            ItemStack item  = this.invHandler.getStackInSlot(i);
            if(item != ItemStack.EMPTY){

            }
        }
    }

    @Override
    public ItemStackHandler getInvHandler() {
        return this.invHandler;
    }

    @Override
    public CustomForgeEnergyStorage getEnergyStorage() {
        return this.storage;
    }
}
