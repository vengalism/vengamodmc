/*
 * Copyright (c) 2017
 */

/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container.slots;

import com.vengalism.vengamodmc.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by vengada on 16/07/2017.
 */
public class SlotItemEnergyBattery extends SlotItemHandler {

    public SlotItemEnergyBattery(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {

        /*if(stack.getItem() == ItemInit.iron_energy_pickaxe){
            return true;
        }*/

        if(stack.getItem() == ItemInit.item_energy_battery){
            return true;
        }
        return stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH);

    }
}