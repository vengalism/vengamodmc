package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

/**
 * Created by vengada at 23/10/2017
 */
public class TileEntityHydroFishTank extends TileEntityHydroNutrientTank implements ITickable{

    private int processingTime = 0;

    public TileEntityHydroFishTank() {
        super();
        this.invHandler = new ItemStackHandler(8);
    }

    @Override
    public void update() {
        if (this.world != null) {
            if (!this.world.isRemote) {
                extractToAdjacent(this.getNutrientTank());
                sync++;
                sync %= 20;
                if(sync == 0) {

                    //processing
                    if(this.fluidTank.getFluidAmount() > 0){
                        if (hasFish() > 0) {
                            processingTime++;
                            this.getNutrientTank().fillInternal(new FluidStack(FluidInit.fluid_nutrient, Config.hydroFishTankFluidGen), true);
                        }
                    }

                    if(processingTime == Config.hydroFishTankMaxTime){
                        //do fish stuff
                        Random chance = new Random();

                        //breed a fish
                        int luck = chance.nextInt(5);
                        if(luck > 4){
                            luck = chance.nextInt(hasFish());
                            manageFish(luck, true);
                        }

                        //kill a fish
                        luck = chance.nextInt(10);
                        if(luck > 9){
                            luck = chance.nextInt(hasFish());
                            manageFish(luck, false);
                        }
                        processingTime = 0;
                    }
                }

            }
        }
    }

    private boolean manageFish(int count, boolean grow){
        for(int i = 0; i < invHandler.getSlots(); i++){
            ItemStack fishStack = getStackInSlot(i);
            if(fishStack.getItem() instanceof ItemFishFood){
                if (grow) {
                    if (fishStack.getCount() + count < fishStack.getMaxStackSize()) {
                        fishStack.grow(count);
                        return true;
                    }
                } else {
                    fishStack.shrink(count);
                    return true;
                }


            }
        }
        return false;
    }

    private ItemStack getStackInSlot(int slot){
        if(invHandler.getStackInSlot(slot) != ItemStack.EMPTY){
            return invHandler.getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    private int hasFish(){
        int fishCount = 0;
        for(int i = 0; i < invHandler.getSlots(); i++){
            Item item = invHandler.getStackInSlot(i).getItem();
            if(item instanceof ItemFishFood){
                fishCount++;
            }
        }
        return fishCount;
    }
}
