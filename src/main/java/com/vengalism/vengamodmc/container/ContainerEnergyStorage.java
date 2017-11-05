/*
 * Copyright (c) 2017
 */
package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotItemEnergyBattery;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyStorage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by vengada on 16/07/2017.
 */
public class ContainerEnergyStorage extends CustomContainer {

    public ContainerEnergyStorage(InventoryPlayer inventoryPlayer, TileEntityEnergyStorage tileEntityEnergyStorage) {
        if (tileEntityEnergyStorage.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = tileEntityEnergyStorage.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

            addSlotToContainer(new SlotItemEnergyBattery(inventory, 0, 62, 35));
            addSlotToContainer(new SlotItemEnergyBattery(inventory, 1, 98, 35));

            addDefaultSlots(inventoryPlayer);
        }
    }
}
