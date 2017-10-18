/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import net.minecraft.item.Item;

public class ItemUtil {


    public static void registerItem(Item item, String name, boolean addTab){
        item.setUnlocalizedName(name);
        item.setRegistryName(name);
        RegistryHandler.ITEMSTOREGISTER.add(item);
        if(addTab) {
            item.setCreativeTab(VengaModMc.vengamodmctab);
        }
    }

}
