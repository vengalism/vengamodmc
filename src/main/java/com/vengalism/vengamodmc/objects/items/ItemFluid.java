/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.capabilities.FluidCapabilityProvider;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemFluid extends ItemBase{
    public int maxFluid, maxReceive, maxExtract;

    public ItemFluid(String name, int maxFluid, int maxReceive, int maxExtract) {
        super(name);
        this.maxFluid = maxFluid;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Total Fluid " + this.getFluidAmount(stack));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private CustomFluidTank getFluidTank(ItemStack itemStack){
        if(itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
            IFluidHandler fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if(fluidHandler instanceof CustomFluidTank){
                return (CustomFluidTank)fluidHandler;
            }
        }
        return new CustomFluidTank(0);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable NBTTagCompound nbt) {
        return new FluidCapabilityProvider(itemStack, this);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        CustomFluidTank fluidTank = getFluidTank(itemStack);
        if (fluidTank != null) {
            double maxAmount = fluidTank.getCapacity();
            double energyDif = maxAmount - fluidTank.getFluidAmount();
            return energyDif / maxAmount;
        }
        return super.getDurabilityForDisplay(itemStack);
    }

    public void setFluid(ItemStack itemStack, FluidStack fluidStack){
        getFluidTank(itemStack).setFluid(fluidStack);
    }

    protected int getFluidAmount(ItemStack itemStack){
        return getFluidTank(itemStack).getFluidAmount();
    }

    protected FluidStack getFluidStack(ItemStack itemStack){
        return getFluidTank(itemStack).getFluid();
    }

    protected int getFluidCapacity(ItemStack itemStack){
        return getFluidTank(itemStack).getFluidAmount();
    }

}
