package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.objects.blocks.BlockHarvester;
import com.vengalism.vengamodmc.util.ItemUtil;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada at 26/01/2018
 */
public class TileEntityHarvester extends TileEntityEnergyBase implements ITickable, ICapabilityProvider {

    private int sync = 0;
    private ItemStackHandler invHandler;
    private int cooldown = 0, maxCooldown = 50;
    private int col, moved = 1, maxMove = 4;


    public TileEntityHarvester(){
        this(20000);
    }


    public TileEntityHarvester(int capacity) {
        super(capacity);
        this.invHandler = new ItemStackHandler(4);
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
            return (T)invHandler;
        }
        return super.getCapability(capability, facing);
    }

    private void moveToNextCrop(){
        moved++;
        if (moved > maxMove) {
            col++;
            moved = 1;
            if (col > maxMove) {
                col = 0;
                moved = 1;
            }
        }
    }

    private void harvestNext(BlockPos cropPos){
        IBlockState cropState = world.getBlockState(cropPos);
        if(cropState.getBlock() instanceof BlockCrops){
            BlockCrops crops = (BlockCrops) cropState.getBlock();
            if(crops.isMaxAge(world.getBlockState(cropPos))){
                NonNullList<ItemStack> drops = NonNullList.create();
                crops.getDrops(drops, this.world, cropPos, cropState, 0);
                for(ItemStack itemStack : drops){
                    ItemUtil.giveItemToContainer(itemStack, 0, this.invHandler);
                }
                world.setBlockState(cropPos, Blocks.AIR.getDefaultState());
                //harvester is not a planter, it only harvests!
            }
        }
        moveToNextCrop();
    }



    @Override
    public void update() {

        if(world != null) {
            if (!world.isRemote) {
                receiveFromAdjacent();
                receiveFromAdjacentRF();
                BlockHarvester blockHarvester;
                if(world.getBlockState(pos).getBlock() instanceof BlockHarvester){
                    blockHarvester = (BlockHarvester)  world.getBlockState(pos).getBlock();
                    EnumFacing facing = blockHarvester.getFacing();
                    int half = maxMove / 2;
                    if (blockHarvester.isPowered(world, pos)) {
                        if (canHarvest()) {
                            if (cooldown == 0) {
                                if (facing == EnumFacing.NORTH) {//z decreases
                                    BlockPos nextPos = new BlockPos((pos.getX() - half) + col, pos.getY(), pos.getZ() - moved);
                                    harvestNext(nextPos);
                                }
                                if (facing == EnumFacing.SOUTH) {//z increases
                                    BlockPos nextPos = new BlockPos((pos.getX() + half) - col, pos.getY(), pos.getZ() + moved);
                                    harvestNext(nextPos);
                                }
                                if (facing == EnumFacing.WEST) {//x is reduced
                                    BlockPos nextPos = new BlockPos(pos.getX() - moved, pos.getY(), (pos.getZ() + half) - col);
                                    harvestNext(nextPos);
                                }
                                if (facing == EnumFacing.EAST) {//x is increased
                                    BlockPos nextPos = new BlockPos(pos.getX() + moved, pos.getY(), (pos.getZ()- half) + col);
                                    harvestNext(nextPos);
                                }
                                cooldown = maxCooldown;
                            } else {
                                //System.out.println(cooldown);
                                cooldown--;
                            }
                        }
                    }
                }


            }
        }
    }

    public boolean canHarvest(){
        return this.storage.getEnergyStored() > 100;
    }

    @Override
    public ItemStackHandler getInvHandler() {
        return this.invHandler;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        moved = compound.getInteger("moved");
        col = compound.getInteger("col");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("moved", moved);
        compound.setInteger("col", col);
        return compound;
    }
}
