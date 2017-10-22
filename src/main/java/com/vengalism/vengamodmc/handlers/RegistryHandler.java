/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.handlers;

import com.vengalism.vengamodmc.init.BlockInit;
import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import com.vengalism.vengamodmc.objects.fluid.FluidStateMapper;
import com.vengalism.vengamodmc.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final ArrayList<Item> ITEMSTOREGISTER = new ArrayList<>();
    public static final ArrayList<Block> BLOCKSTOREGISTER = new ArrayList<>();
    public static final ArrayList<Fluid> FLUIDSTOREGISTER = new ArrayList<>();

    public static void Client(){}

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Item> event){
        ItemInit.init();
        event.getRegistry().registerAll(ITEMSTOREGISTER.toArray(new Item[0]));
    }


    @SubscribeEvent
    public void onBlockRegistry(RegistryEvent.Register<Block> event){
        BlockInit.init();

        event.getRegistry().registerAll(BLOCKSTOREGISTER.toArray(new Block[0]));
    }

    /*
    @SubscribeEvent
    public void onFluidRegistry(FluidRegistry.FluidRegisterEvent event){
        FluidInit.init();
        //for(Fluid fluid : FLUIDSTOREGISTER){}

        event.getRegistry().registerAll(FLUIDSTOREGISTER.toArray(new Block[0]));
    }*/

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

        for(Fluid fluid : FLUIDSTOREGISTER){
           if(fluid instanceof IHasModel){
               ((IHasModel) fluid).registerModels();
               //FluidRegistry.registerFluid(fluid);
               FluidRegistry.addBucketForFluid(fluid);
               registerCustomFluidBlockRenderer(fluid);

           }
        }
    }

    private static void registerCustomFluidBlockRenderer(Fluid fluid){
        Block block = fluid.getBlock();
        Item item = Item.getItemFromBlock(block);
        FluidStateMapper mapper = new FluidStateMapper(fluid);
        ModelLoader.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, mapper);
        ModelLoader.setCustomStateMapper(block, mapper);
    }

}
