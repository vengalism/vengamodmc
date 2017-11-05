package com.vengalism.vengamodmc.objects.fluid;

import com.vengalism.vengamodmc.Reference;
import net.minecraft.util.ResourceLocation;

/**
 * Created by vengada at 18/10/2017
 */
public class FluidNutrient extends FluidBase{

    private int delayBuff = 1000;

    public FluidNutrient(String fluidName, String still, String flowing, int delayBuff) {
        super(fluidName, new ResourceLocation(Reference.MODID, still), new ResourceLocation(Reference.MODID, flowing));
        this.delayBuff = delayBuff;
    }

    @Override
    public String getUnlocalizedName(){
        return "fluid." + Reference.MODID + ":" + this.unlocalizedName;
    }

    public int getDelayBuff(){
        return this.delayBuff;
    }
}
