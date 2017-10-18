/*
 * Copyright (c) 2017
 */

/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.hydro;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

public interface INutrientMixture {

    List<INutrient> getNutrients();
    boolean isValidMixture();
    int getTotalFluid(ItemStack stack);
    int getCurrentFluidStored(ItemStack stack);
    void drainFluid(ItemStack stack, int amount);
    Fluid getFluidType();

}
