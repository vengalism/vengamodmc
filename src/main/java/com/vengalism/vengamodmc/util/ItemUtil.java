/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemUtil {


    public static void registerItem(Item item, String name, boolean addTab){
        item.setUnlocalizedName(name);
        item.setRegistryName(name);
        RegistryHandler.ITEMSTOREGISTER.add(item);
        if(addTab) {
            item.setCreativeTab(VengaModMc.vengamodmctab);
        }
    }

    public static boolean giveItemToContainer(ItemStack itemStack, int slot, ItemStackHandler invHandler) {
        ItemStack result;
        if (invHandler != null) {
            result = invHandler.insertItem(slot, itemStack.copy(), false);
            if (result.isEmpty()) {
                return true;
            } else {
                int nextSlot = slot + 1;
                if (nextSlot < invHandler.getSlots()) {
                    return giveItemToContainer(result, nextSlot, invHandler);
                } else {
                    return false;
                }

            }
        }
        return false;
    }

}
