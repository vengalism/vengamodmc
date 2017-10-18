/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.util.IHasModel;
import com.vengalism.vengamodmc.util.ItemUtil;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        this(name, true);
    }

    public ItemBase(String name, boolean addTab){
        ItemUtil.registerItem(this, name, addTab);
    }

    @Override
    public void registerModels() {
        VengaModMc.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
