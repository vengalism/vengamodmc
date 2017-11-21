/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.objects.blocks.BlockEnergyStorage;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    private Enums.MACHINETIER machinetier;

    public TileEntityEnergyStorage(){
        this(Enums.MACHINETIER.ONE);
    }

    public TileEntityEnergyStorage(Enums.MACHINETIER machinetier){
        this(Config.energyStorageMaxEnergyStored, Config.energyStorageMaxReceive, Config.energyStorageMaxExtractSpeed, machinetier);
    }

    public TileEntityEnergyStorage(int capacity, int maxReceive, int maxExtract, Enums.MACHINETIER machinetier){
        super(capacity * machinetier.getMultiplier(), maxReceive * machinetier.getMultiplier(), maxExtract * machinetier.getMultiplier());
        invHandler = new ItemStackHandler(2);
        this.machinetier = machinetier;
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

                extractToAdjacentRF();

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
            sync++;
            sync %= 20;
            if(sync == 0){
                setState();
            }

        }
    }

    private void setState(){
        if(!world.isRemote) {
            TileEntity te = this.world.getTileEntity(this.pos);
            if(te instanceof TileEntityEnergyStorage){
                BlockEnergyStorage bes = (BlockEnergyStorage)te.getBlockType();
                int per = this.getPercentFull();
                world.setBlockState(this.pos, bes.getStateFromMeta(per));
                this.markDirty();
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        if(!world.isRemote){
            world.notifyBlockUpdate(pos, oldState, newSate, getPercentFull());

        }
        return oldState.getBlock() != newSate.getBlock();

    }

    public int getPercentFull(){

        int capacity = this.storage.getMaxEnergyStored();
        int nrg = this.storage.getEnergyStored();
        int percent = ((nrg * 100) / capacity);

        if (percent <= 9) {
            return 0;
        } else if (percent <= 19) {
            return 1;
        } else if (percent <= 29) {
            return 2;
        } else if (percent <= 39) {
            return 3;
        } else if (percent <= 49) {
            return 4;
        } else if (percent <= 59) {
            return 5;
        } else if (percent <= 69) {
            return 6;
        } else if (percent <= 79) {
            return 7;
        } else if (percent <= 89) {
            return 8;
        } else if (percent <= 99) {
            return 9;
        } else {
            return 10;
        }


    }

    public Enums.MACHINETIER getMachinetier() {
        return this.machinetier;
    }

    @Override
    public ItemStackHandler getInvHandler() {
        return this.invHandler;
    }

    public int getEnergyStored(){
        return this.storage.getEnergyStored();
    }
}
