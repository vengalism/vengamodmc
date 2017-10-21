package com.vengalism.vengamodmc.objects.fluid;

import com.vengalism.vengamodmc.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by vengada at 19/10/2017
 */
public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

    public final Fluid fluid;
    public final ModelResourceLocation resourceLocation;

    public FluidStateMapper(Fluid fluid){
        this.fluid = fluid;
        this.resourceLocation = new ModelResourceLocation(new ResourceLocation(Reference.MODID, "fluids"), fluid.getName());
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return this.resourceLocation;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return this.resourceLocation;
    }
}
