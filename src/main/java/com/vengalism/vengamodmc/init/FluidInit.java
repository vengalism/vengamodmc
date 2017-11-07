package com.vengalism.vengamodmc.init;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.handlers.RegistryHandler;
import com.vengalism.vengamodmc.objects.blocks.BlockFluidBase;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.objects.fluid.LiquidNutrient;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by vengada at 15/10/2017
 */
public class FluidInit {

    public static FluidNutrient fluid_nutrient, fluid_nutrient_oxygenated;

    public static Block block_fluid_nutrient, block_fluid_nutrient_oxygenated;

    public static void init(){



        fluid_nutrient = new FluidNutrient("fluid_nutrient", "blocks/fluid_nutrient_still", "blocks/fluid_nutrient_flow", Config.fluidNutrientDelay);
        registerFluid(fluid_nutrient);

        fluid_nutrient_oxygenated = new FluidNutrient("fluid_nutrient_oxygenated", "blocks/fluid_nutrient_oxygenated_still", "blocks/fluid_nutrient_oxygenated_flow", Config.fluidOxNutrientDelay);
        registerFluid(fluid_nutrient_oxygenated);

        block_fluid_nutrient = new BlockFluidBase("fluid_nutrient", fluid_nutrient, new LiquidNutrient(MapColor.GREEN));
        block_fluid_nutrient_oxygenated = new BlockFluidBase("fluid_nutrient_oxygenated", fluid_nutrient_oxygenated, new LiquidNutrient(MapColor.YELLOW));
    }

    private static void registerFluid(Fluid fluid){
        FluidRegistry.registerFluid(fluid);
        RegistryHandler.FLUIDSTOREGISTER.add(fluid);
    }
}
