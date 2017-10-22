package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import com.vengalism.vengamodmc.util.MyUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
    protected TileEntity[] teAdjacent = new TileEntity[6];

    public TileEntityFluidTankBase(){
        this(Fluid.BUCKET_VOLUME * 2, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));
    }

    public TileEntityFluidTankBase(int capacity, FluidStack fluidStack){
        this.fluidTank = new CustomFluidTank(fluidStack, capacity);
    }

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

    private int calculateSides(ArrayList<IFluidHandler> tiles){
        int count = 0;
        for(IFluidHandler t : tiles){
            if(t != null) {
                count++;
            }
        }
        return count < 1 ? 1 : count;//return 1 or the count
    }

    public void extractToAdjacent(){
        extractToAdjacent(this.fluidTank);
    }

    public void extractToAdjacent(CustomFluidTank tank){
        for(IFluidHandler ifh : getIFHTilesAdjacent()){
            IFluidTankProperties[] properties = ifh.getTankProperties();
            for(IFluidTankProperties prop : properties){
                if(prop.canFill()){
                    if(tank.canDrain()) {

                        exchangeFluid(tank, ifh, tank.getFluidAmount() / calculateSides(getIFHTilesAdjacent()));
                    }
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
            int ifhCurrent = 0;
            boolean can = false;
            for(IFluidTankProperties prop : properties){
                can = prop.canDrain();
                if(prop.getContents() != null){
                    ifhCurrent = prop.getContents().amount;
                }
            }

            if(can){
                if(ifhCurrent > 0){
                    exchangeFluid(ifh, tank, ifhCurrent / getIFHTilesAdjacent().size());
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

    public void setCanFill(boolean canFill){
        this.fluidTank.setCanFill(canFill);
    }

    public void setCanDrain(boolean canDrain){
        this.fluidTank.setCanDrain(canDrain);
    }

    public boolean canFill(){
        return this.fluidTank.canFill();
    }

    public boolean canDrain(){
        return this.fluidTank.canDrain();
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
