/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.handlers;

import com.vengalism.vengamodmc.init.BlockInit;
import com.vengalism.vengamodmc.init.ItemInit;
import com.vengalism.vengamodmc.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final ArrayList<Item> ITEMSTOREGISTER = new ArrayList<>();
    public static final ArrayList<Block> BLOCKSTOREGISTER = new ArrayList<>();

    public static void Client(){}

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Item> event){
        ItemInit.init();
        /*for(Item item : ITEMSTOREGISTER){
            event.getRegistry().register(item);
        }*/
        event.getRegistry().registerAll(ITEMSTOREGISTER.toArray(new Item[0]));
        //ITEMSTOREGISTER.clear();
    }


    @SubscribeEvent
    public void onBlockRegistry(RegistryEvent.Register<Block> event){
        BlockInit.init();
        /*for(Block block : BLOCKSTOREGISTER){
            event.getRegistry().register(block);
        }*/
        event.getRegistry().registerAll(BLOCKSTOREGISTER.toArray(new Block[0]));
    }

    @SubscribeEvent
    public void onCraftingRegistry(RegistryEvent.Register<IRecipe> event){
        RecipeHandler.registerCrafting();
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event){
        for(Item item : ITEMSTOREGISTER){
            if(item instanceof IHasModel){
                ((IHasModel)item).registerModels();
            }
        }

        for(Block block : BLOCKSTOREGISTER){
            if(block instanceof IHasModel){
                ((IHasModel)block).registerModels();
            }
        }
    }
}
