/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by vengada on 18/07/2017.
 */
public class CustomContainer extends Container {

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
            int containerSlots = inventorySlots.size() - playerIn.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(stackInSlot, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stackInSlot, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            slot.onTake(playerIn, stackInSlot);
        }
        return stack;
    }

    void addDefaultSlots(InventoryPlayer inventoryPlayer) {
        //player inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(inventoryPlayer, x + (y * 9) + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        //player hotbar
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + (i * 18), 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
