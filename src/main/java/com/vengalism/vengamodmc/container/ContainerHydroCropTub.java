package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotItemHydroAirStone;
import com.vengalism.vengamodmc.container.slots.SlotItemNutrient;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroCropTub;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by vengada at 22/10/2017
 */
public class ContainerHydroCropTub  extends CustomContainer{

    public ContainerHydroCropTub(InventoryPlayer inventoryPlayer, TileEntityHydroCropTub hydroTubTileEntity){
        if (hydroTubTileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = hydroTubTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            addSlotToContainer(new SlotItemHandler(inventory, 0, 71, 26));
            addSlotToContainer(new SlotItemHandler(inventory, 1, 89, 26));
            addSlotToContainer(new SlotItemHandler(inventory, 2, 71, 44));
            addSlotToContainer(new SlotItemHandler(inventory, 3, 89, 44));

            addSlotToContainer(new SlotItemNutrient(inventory, 4, 8, 26));
            addSlotToContainer(new SlotItemHydroAirStone(inventory, 5, 8, 44));

            addDefaultSlots(inventoryPlayer);
        }
    }
}
