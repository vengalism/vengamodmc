/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.objects.blocks.BlockEnergyStorage;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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
                if(this.machinetier == Enums.MACHINETIER.FOUR){
                    this.storage.setEnergy(this.storage.getMaxEnergyStored());
                }
                extractToAdjacent();

                receiveFromAdjacent();

                extractToAdjacentRF();

                receiveFromAdjacentRF();

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
                this.setState();
            }

        }
    }

    private void setState(){
        if(!world.isRemote) {
            Block te = this.world.getBlockState(pos).getBlock();
            if(te instanceof BlockEnergyStorage){
                BlockEnergyStorage bes = (BlockEnergyStorage) world.getBlockState(pos).getBlock();
                bes.updateState(world, pos, getPercentFull());

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

    private int getPercentFull(){

        int capacity = this.storage.getMaxEnergyStored();
        int nrg = this.storage.getEnergyStored();
        return ((nrg * 10) / capacity);
    }

    @Override
    public Enums.MACHINETIER getMachineTier() {
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
