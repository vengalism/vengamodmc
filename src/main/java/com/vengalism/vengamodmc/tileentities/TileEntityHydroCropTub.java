package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.init.FluidInit;
import com.vengalism.vengamodmc.objects.blocks.BlockHydroCropTub;
import com.vengalism.vengamodmc.objects.fluid.CustomFluidTank;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by vengada at 22/10/2017
 */
public class TileEntityHydroCropTub extends TileEntityFluidTankBase implements ITickable {

    private ItemStackHandler invHandler;
    private final int NUTRIENTMIXTURESLOT = 4, AIRSTONESLOT = 5;
    private boolean outputFull = false;

    public TileEntityHydroCropTub(){
        //super();
        this((Fluid.BUCKET_VOLUME * 2) + 1, new FluidStack(FluidInit.fluid_nutrient, 0));
    }

    public TileEntityHydroCropTub(int capacity, FluidStack fluidStack) {
        super(capacity, fluidStack);
        ArrayList<Fluid> stacks = new ArrayList<>();
        stacks.add(FluidInit.fluid_nutrient_oxygenated);
        stacks.add(FluidInit.fluid_nutrient);
        this.fluidTank.setFluidTypes(stacks);

        this.invHandler = new ItemStackHandler(6);
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
    public CustomFluidTank getFluidTank(){
        return this.fluidTank;
    }

    private int getDelayBuffs(){
        int delay = 5000;

        if(isFluidEqual(this.fluidTank, FluidInit.fluid_nutrient)){
            if(getFluidTank().getFluidAmount() > 0){
                delay -= 2700;
            }
        }else if(isFluidEqual(this.fluidTank, FluidInit.fluid_nutrient_oxygenated)){
            if(getFluidTank().getFluidAmount() > 0){
                delay -= 3400;
            }
        }
        return delay;
    }


    private boolean isFluidEqual(FluidTank fluidTank, Fluid fluid){
        if(fluidTank.getFluid() != null){
            if(fluidTank.getFluid().getFluid() != null){
                return fluidTank.getFluid().getFluid() == fluid;
            }
        }
        return false;
    }

    private void drainNutrients(boolean harvested){
        FluidStack fluidType = this.fluidTank.getFluid();
        if(fluidType != null){
            fluidType = new FluidStack(fluidType.getFluid(), 2);
            this.fluidTank.drainInternal(fluidType, true);
        }
        if(harvested){
            if(fluidType != null) {
                fluidType = new FluidStack(fluidType.getFluid(), 6);
                this.fluidTank.drainInternal(fluidType, true);
            }
        }
    }

    private void harvestCropAbove(IBlockState cropAbove, BlockPos above){
        if (cropAbove.getBlock() instanceof BlockCrops) {
            BlockCrops crops = (BlockCrops) cropAbove.getBlock();

            IBlockState defaultState = crops.getDefaultState();

            if (crops.isMaxAge(world.getBlockState(above))) {
                NonNullList<ItemStack> drops = NonNullList.create();
                cropAbove.getBlock().getDrops(drops, this.world, above, cropAbove, 0);
                boolean placed = false;
                boolean takenSeed = false;
                for (ItemStack itemStack : drops) {
                    if (itemStack.getItem() instanceof IPlantable) {

                        if (!placed) {
                            world.setBlockState(above, defaultState);
                            placed = true;
                        }
                    }
                    for(int i = 0; i < this.invHandler.getSlots(); i++){
                        if(i != NUTRIENTMIXTURESLOT || i != AIRSTONESLOT) {
                            if (!takenSeed && itemStack.getItem() instanceof IPlantable) {
                                itemStack.shrink(1);
                                takenSeed = true;
                            } else {
                                int beforeCount = itemStack.getCount();

                                //insert only if its an empty slot, or the current count + insertcount < stacksize
                                if (this.invHandler.getStackInSlot(i).isEmpty() || this.invHandler.getStackInSlot(i).getCount() + beforeCount < this.invHandler.getStackInSlot(i).getMaxStackSize()) {
                                    ItemStack result = this.invHandler.insertItem(i, itemStack.copy(), false);

                                    if (result.isEmpty()) {
                                        itemStack.shrink(beforeCount);
                                    } else {
                                        int afterCount = result.getCount();
                                        itemStack.shrink(beforeCount - afterCount);
                                        this.outputFull = true;
                                    }
                                    drainNutrients(true);
                                }


                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (this.world != null) {

            if (!this.world.isRemote) {
                //receiveFromAdjacent();
                extractToAdjacent();

                sync++;
                sync %= 20;
                if (sync == 0) {
                    drainNutrients(false);
                    //System.out.println(this.fluidTank.getFluid().getUnlocalizedName());
                    BlockPos above = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                    IBlockState cropAbove = this.world.getBlockState(above);
                    this.world.scheduleBlockUpdate(above, cropAbove.getBlock(), this.getDelayBuffs(), 10);
                    if (!isOutputFull()) {
                        this.harvestCropAbove(cropAbove, above);
                    } else {
                        for (int i = 0; i < this.invHandler.getSlots(); i++) {
                            if (this.invHandler.getStackInSlot(i).isEmpty()) {
                                this.outputFull = false;
                            }
                        }
                    }
                    setState(this.world, this.pos);
                }
            }
        }
    }

    private void setState(World world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityHydroCropTub){
            int level = 0;
            int filledAmount = this.getFluidTank().getFluidAmount();
            if(filledAmount > 1500){
                level = 3;
            }else if(filledAmount > 1000 && filledAmount < 1500){
                level = 2;
            }else if(filledAmount > 500 && filledAmount < 1000){
                level = 1;
            }else if(filledAmount > 0 && filledAmount < 500){
                level = 0;
            }
            TileEntityHydroCropTub hydroTubTileEntity = (TileEntityHydroCropTub)te;
            BlockHydroCropTub hydroTubBlock = (BlockHydroCropTub) hydroTubTileEntity.getBlockType();
            //hydroTubBlock.setFluidLevel(world, pos, hydroTubBlock.getDefaultState(), level);
            world.setBlockState(this.pos, hydroTubBlock.getStateFromMeta(level));
            this.markDirty();
        }
    }

    private boolean isOutputFull(){
        return this.outputFull;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return (oldState.getBlock()) != newSate.getBlock();
    }

    @Override
    public ItemStackHandler getInvHandler() {
        return this.invHandler;
    }
}
