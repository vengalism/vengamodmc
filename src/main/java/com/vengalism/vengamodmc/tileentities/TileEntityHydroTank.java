package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.init.ItemInit;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import com.vengalism.vengamodmc.objects.fluid.FluidNutrient;
import com.vengalism.vengamodmc.objects.items.ItemHydroAirStone;
import com.vengalism.vengamodmc.objects.items.ItemNutrientMixture;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by vengada at 20/10/2017
 */
public class TileEntityHydroTank extends  TileEntityFluidTankBase implements ITickable{

    private ItemStackHandler invHandler;
    private CustomFluidTank nutrientTank;
    private final int AIRSTONESLOT = 1, NUTRIENTMIXTURESLOT = 0;

    public TileEntityHydroTank() {
        this(Fluid.BUCKET_VOLUME * 2, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));
    }

    public TileEntityHydroTank(int capacity, FluidStack afluid){
        super(capacity, afluid);
        this.invHandler = new ItemStackHandler(2);
        setCanFill(true);
        setCanDrain(false);

        this.nutrientTank = new CustomFluidTank(Fluid.BUCKET_VOLUME * 6);
        ArrayList<Fluid> stacks = new ArrayList<>();
        stacks.add(FluidInit.fluid_nutrient_oxygenated);
        stacks.add(FluidInit.fluid_nutrient);
        this.nutrientTank.setFluidTypes(stacks);
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

    private void upkeep(){
        Fluid currFluid;
        int amount = 1;
        if(getNutrientMixture() != null){
            currFluid  = getNutrientMixture().getFluidType();
            amount = getNutrientMixture().getCurrentFluidStored(getStackInSlot(NUTRIENTMIXTURESLOT));
            getNutrientMixture().setFluid(getStackInSlot(NUTRIENTMIXTURESLOT), new FluidStack(currFluid, amount - 1).copy());
        }

        if(hasAirStone()) {
            if (getHydroAirStone() != null) {
                int currEnergy = getHydroAirStone().getEnergyStored(getStackInSlot(AIRSTONESLOT));
                if(currEnergy > 0){
                    //System.out.println(currEnergy + " curren");
                    getHydroAirStone().setEnergy(getStackInSlot(AIRSTONESLOT), currEnergy - 1);
                }
            }
        }
    }

    @Override
    public void update() {
        //take in water
        //receiveFromAdjacent();
        //give out nutrients
        extractToAdjacent(this.nutrientTank);
        sync++;
        sync %= 20;
        if(sync == 0){

            if(this.fluidTank.canFill()){ //auto fill water tank
                this.fluidTank.fillInternal(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
            }
            //processing
            if(hasNutrientMixture()){
//                ItemNutrientMixture mixture = getNutrientMixture();
                FluidStack proNutrient;
                if(hasAirStone()){ //fill with oxygenated nutrient fluid
                    proNutrient = new FluidStack(FluidInit.fluid_nutrient_oxygenated, 50);
                }else{ //fill with regular nutrient fluid
                    proNutrient = new FluidStack(FluidInit.fluid_nutrient, 50);
                }

                if (this.fluidTank.getFluidAmount() >= 50) {
                    int after = this.nutrientTank.fillInternal(proNutrient.copy(), false);
                    //if 0 it didnt add more to the tank, so dont do upkeep
                    if(after != 0){
                        this.nutrientTank.fillInternal(proNutrient.copy(), true);
                        this.fluidTank.drain(50, true);
                        upkeep();
                    }

                }

                if(getNutrientMixture() != null) {
                    if (getNutrientMixture().getCurrentFluidStored(getStackInSlot(NUTRIENTMIXTURESLOT)) <= 0) {
                        System.out.println("less then 0, removing mixture");
                        getStackInSlot(NUTRIENTMIXTURESLOT).shrink(1);
                    }
                }
            }

        }
    }

    private ItemStack getStackInSlot(int slot){
        return this.invHandler.getStackInSlot(slot);
    }

    public CustomFluidTank getNutrientTank() {
        return this.nutrientTank;
    }

    //AIR STONE
    private boolean hasAirStone(){
        return getStackInSlot(AIRSTONESLOT) != ItemStack.EMPTY;
    }

    private ItemHydroAirStone getHydroAirStone(){
        if(hasAirStone()){
            Item item = getStackInSlot(AIRSTONESLOT).getItem();
            if(item instanceof ItemHydroAirStone){
                return (ItemHydroAirStone) item;
            }
        }
        return null;
    }

    //NUTRIENT MIXTURE ITEM
    private boolean hasNutrientMixture(){
        return getStackInSlot(NUTRIENTMIXTURESLOT) != ItemStack.EMPTY;
    }

    private ItemNutrientMixture getNutrientMixture(){
        if(hasNutrientMixture()){
            Item item = getStackInSlot(NUTRIENTMIXTURESLOT).getItem();
            if(item instanceof ItemNutrientMixture) {
                return (ItemNutrientMixture) item;
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.invHandler.deserializeNBT(compound.getCompoundTag("invHandler"));
        this.nutrientTank.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("invHandler", this.invHandler.serializeNBT());
        this.nutrientTank.writeToNBT(compound);
        return compound;
    }



}
