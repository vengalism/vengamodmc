/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada at 15/10/2017
 */
public class TileEntityEnergyStorage extends TileEntityEnergyBase implements ICapabilityProvider, ITickable{

    private static final int DISCHARGESLOT = 0, CHARGESLOT = 1;
    private ItemStackHandler invHandler;

    public TileEntityEnergyStorage() {
        this(50000, 1000);
    }

    public TileEntityEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        invHandler = new ItemStackHandler(2);
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
            return (T) this.invHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (this.world != null) {

            if (!this.world.isRemote) {
                extractToAdjacent();

                receiveFromAdjacent();

                //extract from item receive to me
                ItemStack disItem = this.invHandler.getStackInSlot(DISCHARGESLOT);
                if (disItem != ItemStack.EMPTY) {
                    receiveFromItem(disItem);
                }

                //receive to item aka give to item, extract from me
                ItemStack recItem = this.invHandler.getStackInSlot(CHARGESLOT);
                if (recItem != ItemStack.EMPTY) {
                    extractToItem(recItem);
                }
            }
        }
    }

    @Override
    public ItemStackHandler getInvHandler() {
        return this.invHandler;
    }
}
