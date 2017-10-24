package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotItemHydroAirStone;
import com.vengalism.vengamodmc.container.slots.SlotItemNutrient;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroNutrientTank;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerHydroTank extends CustomContainer {

    public ContainerHydroTank(InventoryPlayer inventoryPlayer, TileEntityHydroNutrientTank hydroTankTileEntity){
        if (hydroTankTileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = hydroTankTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

            addSlotToContainer(new SlotItemNutrient(inventory, 0, 8, 26));
            addSlotToContainer(new SlotItemHydroAirStone(inventory, 1, 8, 44));

            addDefaultSlots(inventoryPlayer);
        }
    }
}
