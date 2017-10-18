/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.vengalism.vengamodmc.capabilities.EnergyCapabilityProvider;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ToolEnergy extends ToolBase {

    //private CustomForgeEnergyStorage storage;
    public int maxEnergyStored;
    public int maxExtract, maxReceive;

    protected ToolEnergy(String name, float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn, boolean defaultEnergyStorage) {
        super(name, attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
        if(defaultEnergyStorage){
            this.maxEnergyStored = 10000;
            this.maxExtract = 100;
            this.maxReceive = 1000;
        }
    }


    public ToolEnergy(String name, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn, boolean defaultEnergyStorage) {
        super(name, materialIn, effectiveBlocksIn);
        if(defaultEnergyStorage){
            this.maxEnergyStored = 10000;
            this.maxExtract = 100;
            this.maxReceive = 1000;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(String.format("Energy " + this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {

        return new EnergyCapabilityProvider(stack, this);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        if(stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)){
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

}
