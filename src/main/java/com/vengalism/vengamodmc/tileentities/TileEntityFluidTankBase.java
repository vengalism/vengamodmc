package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import com.vengalism.vengamodmc.util.MyUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by vengada at 20/10/2017
 */
public class TileEntityFluidTankBase extends TileEntityBase{

    protected CustomFluidTank fluidTank;

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return (T) this.fluidTank;
        }
        return super.getCapability(capability, facing);
    }

    private void exchangeFluid(IFluidHandler from, IFluidHandler to, int fluidAmount){

        FluidStack dranedFrom = from.drain(fluidAmount, false);
        if(dranedFrom != null){
            int filled = to.fill(dranedFrom.copy(), true);
            from.drain(filled, true);
        }
    }



    public void extractToAdjacent(){
        extractToAdjacent(this.fluidTank);
    }

    public void extractToAdjacent(CustomFluidTank tank){
        for(IFluidHandler ifh : getIFHTilesAdjacent()){
            IFluidTankProperties[] properties = ifh.getTankProperties();
            for(IFluidTankProperties prop : properties){
                if(prop.canFill()){
                    exchangeFluid(tank, ifh, Fluid.BUCKET_VOLUME);
                }
            }

        }
    }

    public void receiveFromAdjacent(){
        receiveFromAdjacent(this.fluidTank);
    }
    public void receiveFromAdjacent(CustomFluidTank tank){
        for(IFluidHandler ifh : getIFHTilesAdjacent()){
            IFluidTankProperties[] properties = ifh.getTankProperties();
            for(IFluidTankProperties prop : properties){
                if(prop.canDrain()){
                    exchangeFluid(ifh, tank, Fluid.BUCKET_VOLUME);
                }
            }

        }
    }

    //get all IFluid Handlers adjacent
    private ArrayList<IFluidHandler> getIFHTilesAdjacent(){
        ArrayList<IFluidHandler> adjacentIFH = new ArrayList<>();
        for(TileEntity tileEntity : MyUtil.getAdjacentBlocks(this.world, this.pos)){
            if(tileEntity != null){
                if(tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
                    IFluidHandler ifh = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                    adjacentIFH.add(ifh);
                }
            }
        }
        return adjacentIFH;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.fluidTank.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.fluidTank.writeToNBT(compound);
        return compound;
    }

}
