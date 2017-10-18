/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotVaultGenerator;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by vengada on 18/07/2017.
 */
public class ContainerEnergyGenerator extends CustomContainer {

    public ContainerEnergyGenerator(InventoryPlayer inventoryPlayer, TileEntityEnergyGenerator tileEntityEnergyGenerator) {
        if (tileEntityEnergyGenerator.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = tileEntityEnergyGenerator.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

            addSlotToContainer(new SlotVaultGenerator(inventory, 0, 80, 35));

            addDefaultSlots(inventoryPlayer);
        }
    }
}
