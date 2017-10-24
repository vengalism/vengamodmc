package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.container.slots.SlotItemFishFood;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroFishTank;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by vengada at 24/10/2017
 */
public class ContainerHydroFishTank extends CustomContainer{

    public ContainerHydroFishTank(InventoryPlayer inventoryPlayer, TileEntityHydroFishTank hydroTankTileEntity){
        if (hydroTankTileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            IItemHandler inventory = hydroTankTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            int count = 0;
            for(int y = 0; y < 2; y++) {
                for (int i = 0; i < 4; i++) {
                    //System.out.println("count: " + count + " y: " + y + " i: " + i);
                    addSlotToContainer(new SlotItemFishFood(inventory, count, 53 + (i * 18), 26 + (y * 18)));
                    count++;
                }
            }


            addDefaultSlots(inventoryPlayer);
        }
    }

}
