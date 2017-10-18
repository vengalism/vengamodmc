/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class BlockUtil {

    public static void registerBlock(Block block, Item itemBlock, String name, boolean addTab){
        block.setUnlocalizedName(name);
        block.setRegistryName(name);
        RegistryHandler.BLOCKSTOREGISTER.add(block);

        itemBlock.setRegistryName(block.getRegistryName() != null ? block.getRegistryName() : new ResourceLocation("uhoh"));
        RegistryHandler.ITEMSTOREGISTER.add(itemBlock);
        if (addTab) {
            block.setCreativeTab(VengaModMc.vengamodmctab);
        }
    }
}
