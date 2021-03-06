/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotItemEnergyBattery;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyFurnace;
import com.vengalism.vengamodmc.util.Enums;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnergyFurnace extends CustomContainer {

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
    }

    public ContainerEnergyFurnace(InventoryPlayer inventoryPlayer, TileEntityEnergyFurnace tileEntityEnergyFurnace) {

        if (tileEntityEnergyFurnace.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = tileEntityEnergyFurnace.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            Enums.MACHINETIER tier = tileEntityEnergyFurnace.getMachinetier();
            switch(tier){
                case THREE:
                    //tier 3
                    addSlotToContainer(new SlotItemHandler(inventory, 0, 44, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 1, 44, 53));
                    addSlotToContainer(new SlotItemHandler(inventory, 3, 80, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 4, 80, 53));
                    addSlotToContainer(new SlotItemHandler(inventory, 5, 116, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 6, 116, 53));
                    break;
                case TWO:
                    //tier 2
                    addSlotToContainer(new SlotItemHandler(inventory, 0, 44, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 1, 44, 53));
                    addSlotToContainer(new SlotItemHandler(inventory, 3, 80, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 4, 80, 53));
                    break;
                case ONE:
                default:
                    //tier 1
                    addSlotToContainer(new SlotItemHandler(inventory, 0, 44, 17));
                    addSlotToContainer(new SlotItemHandler(inventory, 1, 44, 53));
                    break;
            }

            //energy
            addSlotToContainer(new SlotItemEnergyBattery(inventory, 2, 8, 53));

            addDefaultSlots(inventoryPlayer);
        }
    }
}
