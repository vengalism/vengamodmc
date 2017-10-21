package com.vengalism.vengamodmc.objects.fluid;

import com.vengalism.vengamodmc.util.IHasModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;


/**
 * Created by vengada at 19/10/2017
 */ //fluidcolored/fluidMolten
public class FluidBase extends Fluid implements IHasModel {

    public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
    }

    @Override
    public void registerModels() {
//        FluidRegistry.registerFluid(this);
//        System.out.println("add bucket success " + FluidRegistry.addBucketForFluid(this));
        //FluidRegistry.addBucketForFluid(this);
    }




}
