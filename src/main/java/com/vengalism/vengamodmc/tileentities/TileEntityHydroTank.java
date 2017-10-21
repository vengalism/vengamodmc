package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.objects.items.ItemHydroAirStone;
import com.vengalism.vengamodmc.objects.items.ItemNutrientMixture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada at 20/10/2017
 */
public class TileEntityHydroTank extends  TileEntityFluidTankBase implements ITickable{

    private ItemStackHandler invHandler;
    private CustomFluidTank nutrientTank;
    private final int AIRSTONESLOT = 0, NUTRIENTMIXTURESLOT = 1;
    private boolean hasAirStone = false, hasNutrientMixture = false;

    public TileEntityHydroTank() {
        this(Fluid.BUCKET_VOLUME * 2, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));
    }

    public TileEntityHydroTank(int capacity, FluidStack afluid){
        this.invHandler = new ItemStackHandler(2);
        this.fluidTank = new CustomFluidTank(afluid, capacity);
        this.fluidTank.setCanFill(true);
        this.fluidTank.setCanDrain(false);

        this.nutrientTank = new CustomFluidTank(new FluidStack(FluidInit.fluid_nutrient, 0),Fluid.BUCKET_VOLUME * 6);
        this.nutrientTank.setCanFill(false);
        this.nutrientTank.setCanDrain(true);

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return (T) this.invHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        //take in water
        receiveFromAdjacent();
        //give out nutrients
        extractToAdjacent(this.nutrientTank);
        sync++;
        sync %= 20;
        if(sync == 0){
            /*
            if(this.fluidTank.canFill()){ //auto fill water tank
                this.fluidTank.fillInternal(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
            }
            */

            //processing
            if(hasNutrientMixture()){
                FluidStack proNutrient = null;
                if(hasAirStone()){ //fill with oxygenated nutrient fluid
                    //proNutrient = new FluidStack(FluidInit.fluid_nutrient_oxygenated, 50);
                    if(getHydroAirStone() != null) {
                        getHydroAirStone().doUpkeep();
                    }else{
                        System.out.println("ERRO ERRRROOOOO Hydro Air Stone");
                    }
                }else{ //fill with regular nutrient fluid
                    proNutrient = new FluidStack(FluidInit.fluid_nutrient, 50);
                }
                if(this.fluidTank.getFluidAmount() >= 50) {
                    this.fluidTank.drain(50, true);
                    this.nutrientTank.fill(proNutrient, true);
                    getNutrientMixture().drainFluid(this.invHandler.getStackInSlot(NUTRIENTMIXTURESLOT), 1);
                }
                if(getHydroAirStone() != null) {
                    if (getHydroAirStone().getLifespan() <= 0) {
                        this.invHandler.getStackInSlot(AIRSTONESLOT).shrink(1);
                    }
                }else{
                    System.out.println("Error error hydro stone ");
                }
            }

        }
    }
    //AIR STONE
    private boolean hasAirStone(){
        return this.invHandler.getStackInSlot(AIRSTONESLOT) != ItemStack.EMPTY;
    }

    private ItemHydroAirStone getHydroAirStone(){
        if(hasAirStone()){
            return (ItemHydroAirStone) this.invHandler.getStackInSlot(AIRSTONESLOT).getItem();
        }
        return null;
    }

    //NUTRIENT MIXTURE ITEM
    private boolean hasNutrientMixture(){
        return this.invHandler.getStackInSlot(NUTRIENTMIXTURESLOT) != ItemStack.EMPTY;
    }

    private ItemNutrientMixture getNutrientMixture(){
        if(hasNutrientMixture()){
            return (ItemNutrientMixture) this.invHandler.getStackInSlot(NUTRIENTMIXTURESLOT).getItem();
        }
        return null;
    }





}
