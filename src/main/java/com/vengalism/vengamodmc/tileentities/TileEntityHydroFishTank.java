package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by vengada at 23/10/2017
 */
public class TileEntityHydroFishTank extends TileEntityHydroNutrientTank implements ITickable{

    private int processingTime = 0;
    private int maxTime = 1000;

    public TileEntityHydroFishTank() {
        super();
        this.invHandler = new ItemStackHandler(8);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.invHandler.deserializeNBT(compound.getCompoundTag("invHandlera"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("invHandlera", this.invHandler.serializeNBT());
        return compound;
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
                        if (hasFish()) {
                            processingTime++;
                            this.getNutrientTank().fillInternal(new FluidStack(FluidInit.fluid_nutrient, 50), true);
                        }
                    }


                    if(processingTime == maxTime){
                        //do fish stuff
                        processingTime = 0;
                    }
                }

            }
        }
    }

    private boolean hasFish(){
        for(int i = 0; i < invHandler.getSlots(); i++){
            Item item = invHandler.getStackInSlot(i).getItem();
            if(item instanceof ItemFishFood){
                return true;
            }
        }
        return false;
    }
}
