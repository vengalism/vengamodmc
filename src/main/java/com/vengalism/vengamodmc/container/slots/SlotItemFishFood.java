package com.vengalism.vengamodmc.container.slots;

import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by vengada at 24/10/2017
 */
public class SlotItemFishFood extends SlotItemHandler {


    public SlotItemFishFood(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(stack.getItem() instanceof ItemFishFood){
            return true;
        }
        return false;
    }

}
