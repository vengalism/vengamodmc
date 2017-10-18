/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.capabilities;

import com.vengalism.vengamodmc.fluid.CustomFluidTank;

import com.vengalism.vengamodmc.hydro.INutrientMixture;
import com.vengalism.vengamodmc.objects.items.ItemFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidCapabilityProvider implements ICapabilityProvider {

    private CustomFluidTank fluidTank;

    public FluidCapabilityProvider(ItemStack itemStack, ItemFluid fluidItem){
        boolean startMax = false;
        if(fluidItem instanceof INutrientMixture) {
            startMax = true;
        }

        this.fluidTank = new CustomFluidTank(fluidItem.maxFluid, startMax) {
            @Override
            public int getFluidAmount() {
                if (itemStack.hasTagCompound()) {
                    return itemStack.getTagCompound().getInteger("Fluid");
                } else {
                    return 0;
                }
            }

            @Override
            public void setFluid(@Nullable FluidStack fluid) {
                if(fluid != null && itemStack != null) {
                    if (!itemStack.hasTagCompound()) {
                        itemStack.setTagCompound(new NBTTagCompound());
                    }
                    itemStack.getTagCompound().setInteger("Fluid", fluid.amount);
                }
            }
        };
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return (T)this.fluidTank;
        }
        return null;
    }

}
