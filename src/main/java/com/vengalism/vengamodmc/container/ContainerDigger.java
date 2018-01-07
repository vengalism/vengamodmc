package com.vengalism.vengamodmc.container;

import com.vengalism.vengamodmc.tileentities.TileEntityDigger;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Created by vengada at 6/01/2018
 */
public class ContainerDigger extends CustomContainer {


    public ContainerDigger(InventoryPlayer inventoryPlayer, TileEntityDigger tileEntityDigger){
        if(tileEntityDigger.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)){
            IItemHandler inventory = tileEntityDigger.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

            //addSlotToContainer(new Slot(inventory, 1, 1, 1));
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 8; x++) {
                    addSlotToContainer(new SlotItemHandler(inventory, x + (y * 8), 8 + x * 18, 16 + y * 18));
                    //-- y = 0 -- x = 0, 1, 2, 3, 4, 5, 6, 7
                    //--y = 1  == x = 8, 9, 10, 11, 12, 13, 14, 15
                    //--y = 2 --- x = 0 + (2 * 8) = 16, 17, 18, 19, 20, 21, 22, 23
                }
            }

            addDefaultSlots(inventoryPlayer);
        }

    }
}
