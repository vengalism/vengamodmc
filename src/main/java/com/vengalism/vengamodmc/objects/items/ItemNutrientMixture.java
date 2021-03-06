/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.items;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.hydro.INutrient;
import com.vengalism.vengamodmc.hydro.INutrientMixture;
import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vengada at 15/10/2017
 */
public class ItemNutrientMixture extends ItemFluid implements INutrientMixture {

    private List<INutrient> nutrients;
    private final int validWorth = 200;
    private int upkeepCost = 1;

    public ItemNutrientMixture(String name){
        this(name, new ArrayList<>(Arrays.asList(ItemInit.item_nutrient_a, ItemInit.item_nutrient_a, ItemInit.item_nutrient_b, ItemInit.item_nutrient_b)));
    }

    public ItemNutrientMixture(String name, List<INutrient> nutrients){
        super(name, 200, 0, Config.itemNutrientMixtureUpkeepCost);
        this.upkeepCost = upkeepCost; //aka max extract
        this.setMaxStackSize(1);
        this.nutrients = nutrients;
        if(isValidMixture()){
            this.setFluid(new ItemStack(this, 1, 1), new FluidStack(FluidRegistry.WATER, validWorth));
        }else{
            this.setFluid(new ItemStack(this, 1, 1), new FluidStack(FluidRegistry.WATER, 100));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(stack.getItem() instanceof  ItemNutrientMixture){
            ItemNutrientMixture mixture = (ItemNutrientMixture) stack.getItem();
            for (INutrient nutrient : mixture.getNutrients()) {
                tooltip.add("Name: " + nutrient.getName() + " Worth: " + nutrient.getWorth());
            }
            tooltip.add("crafted with any combination(4) of Grow A and B. i.e. 1A, 3B, or 2A, 2B etc etc");

        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public boolean hasFluid(ItemStack stack){
        return this.getCurrentFluidStored(stack) > 0;
    }

    @Override
    public List<INutrient> getNutrients(){
        return this.nutrients;
    }

    @Override
    public boolean isValidMixture(){
        int totalWorth = 0;
        for (INutrient nutrient : getNutrients()) {
            totalWorth+= nutrient.getWorth();
        }

        return totalWorth == validWorth;
    }

    @Override
    public int getTotalFluid(ItemStack stack) {
        if(isValidMixture()) {
            return this.getFluidCapacity(stack);
        }
        return 0;
    }

    @Override
    public int getCurrentFluidStored(ItemStack stack) {
        return this.getFluidAmount(stack);
    }

    @Override
    public void drainFluid(ItemStack stack, int amount) {
        if(isValidMixture()) {
            FluidStack currentFluidStack = this.getFluidStack(stack);
            this.setFluid(stack, new FluidStack(currentFluidStack.getFluid(), currentFluidStack.amount - amount));
        }
    }

    @Override
    public Fluid getFluidType() {
        return FluidInit.fluid_nutrient;
        //return FluidInit.nutrientFluid;
    }

    public int getUpkeepCost(){
        return this.upkeepCost;
    }

}
