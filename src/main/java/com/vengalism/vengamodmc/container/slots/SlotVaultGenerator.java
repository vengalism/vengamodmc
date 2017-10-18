/*
 * Copyright (c) 2017
 */

/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container.slots;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by vengada on 18/07/2017.
 */
//basically the Default SlotFurnaceFuel from Minecraft core, main difference is this takes itemhandler instead of IInventory
public class SlotVaultGenerator extends SlotItemHandler {

    public SlotVaultGenerator(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    private static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack) || isBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }
}
