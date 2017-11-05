/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.energy.EnergyTransfer;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada on 18/07/2017.
 */
public class TileEntityEnergyGenerator extends TileEntityEnergyBase implements ITickable {

    //discharge this item/give item to storage : Chargeslot = charge this item, give item energy from slot
    private static final int BURNSLOT = 0;
    //100k
    private ItemStackHandler invHandler;
    private int cooldown = 0;
    private Enums.MACHINETIER machinetier;

    public TileEntityEnergyGenerator(){
        this(Enums.MACHINETIER.ONE);
    }

    public TileEntityEnergyGenerator(Enums.MACHINETIER machinetier){
        this(Config.energyGeneratorMaxEnergyStored, 0, Config.energyGeneratorEnergyExtractSpeed, machinetier);
    }

    public TileEntityEnergyGenerator(int capacity, int maxReceive, int maxExtract, Enums.MACHINETIER machinetier) {
        super(capacity * machinetier.getMultiplier(), maxReceive * machinetier.getMultiplier(), maxExtract * machinetier.getMultiplier());
        this.machinetier = machinetier;
        this.invHandler = new ItemStackHandler(1);
        this.storage.canReceive();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.invHandler;
        }
        return super.getCapability(capability, facing);
    }

    public void setStorage(int energy) {

        this.storage.setEnergy(energy);
    }

    @Override
    public void update() {

        //int maxCanReceive = this.storage.getMaxCanReceive(); 000000000000
        //pass energy to adjacent blocks, do not receive from adjacent
        //check for item to  burn and generate energy

        if (this.world != null) {

            if (!this.world.isRemote) {
                this.cooldown--;

                sync++;
                sync %= 20;
                if (sync == 0) {
                    //extractToAdjacent();

                    ItemStack fuelItem = this.invHandler.getStackInSlot(BURNSLOT);

                    if (this.cooldown <= 0) {
                        if (fuelItem.getCount() > 0) {
                            this.cooldown = TileEntityFurnace.getItemBurnTime(fuelItem);
                            fuelItem.shrink(1);
                        }
                    } else {

                        this.storage.receiveInternalEnergy(Config.energyGeneratorEnergyPerTick * machinetier.getMultiplier(), false);
                    }
                }
            }
        }
    }

    public Enums.MACHINETIER getMachinetier() {
        return this.machinetier;
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
