/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container.slots;

import com.vengalism.vengamodmc.objects.items.ItemHydroAirStone;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotItemHydroAirStone extends SlotItemHandler {

    public SlotItemHydroAirStone(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof ItemHydroAirStone;
    }
}
