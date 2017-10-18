/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

public class ItemTransfer {

    public static ArrayList<TileEntity> getTiles(World world, BlockPos pos, EnumFacing facing){
        ArrayList<TileEntity> itemTiles = new ArrayList<>();

        for(TileEntity tileEntity : MyUtil.getAdjacentBlocks(world, pos)){
            if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                IItemHandler storage = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if(storage != null) {
                    itemTiles.add(tileEntity);
                }
            }
        }

        return itemTiles;
    }

    public static void giveItemToAdjacent(ItemStack output, IItemHandler to){
        int beforeCount = output.getCount();
        if(to != null){
            for(int i = 0; i < to.getSlots(); i++){

                ItemStack result = to.insertItem(i, output.copy(), false);

                if (result.isEmpty()) {
                    output.shrink(beforeCount);
                } else {
                    int afterCount = result.getCount();
                    output.shrink(beforeCount - afterCount);
                }

            }
        }
    }
}
