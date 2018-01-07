package com.vengalism.vengamodmc.tileentities;

import com.google.gson.JsonObject;
import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.objects.blocks.BlockDigger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by vengada at 6/01/2018
 */
public class TileEntityDigger extends TileEntityEnergyBase implements ITickable {

    private int cooldown = 20; //ticks
    private int maxCooldown = 20;
    private int energyPerUse = 100;
    private int moved = 1, maxMove = 16, col = 0, why = 0;
    private ItemStackHandler invHandler;
    private BlockDigger blockDigger;
    private boolean done = false;
    private String errormsg = "";

    public TileEntityDigger() {
        this(Config.diggerMaxEnergyStored);
    }

    public TileEntityDigger(int capacity) {
        super(capacity);
        this.invHandler = new ItemStackHandler(24);
        this.storage.canReceive();
        this.maxMove = Config.diggerMaxMove;
        this.maxCooldown = Config.diggerMaxCoolDown;
        this.energyPerUse = Config.diggerEnergyPerUse;
        this.cooldown = this.maxCooldown;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.invHandler;
        }
        return super.getCapability(capability, facing);
    }

    private boolean hasEmptyInvSlot() {
        for (int i = 0; i < invHandler.getSlots(); i++) {
            if (invHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                return true;
            }
        }
        return false;
    }

    private boolean canHarvest() {
        if (!done) {
            if (this.storage.getEnergyStored() > energyPerUse) {
                if (hasEmptyInvSlot()) {
                    errormsg = "";
                    return true;
                } else {
                    errormsg = "No Empty Inventory Slots";
                    return false;
                }
            } else {
                errormsg = "Not Enough Energy";
                return false;
            }
        } else {
            errormsg = "All Done!!";
            return false;
        }
    }

    private void harvestNext(BlockPos nextPos) {
        Block block = world.getBlockState(nextPos).getBlock();
        if ((block != Blocks.AIR) && (block != Blocks.BEDROCK)) {
            //if we cant add item to inventory, dont break the block / turn to air
            if(giveItemToContainer(new ItemStack(Item.getItemFromBlock(block), 1), 0)){
                this.storage.extractEnergy(energyPerUse, false);
                world.setBlockState(nextPos, Blocks.AIR.getDefaultState());
            }
        }
        moved++;
        if (moved > maxMove) {
            col++;
            moved = 1;
            if (col > maxMove) {
                col = 0;
                why++;
                moved = 1;
                if (pos.getY() - why <= 1) {
                    done = true;
                    errormsg = "All Done";
                }
            }
        }
    }

    @Override
    public void update() {
        if (this.world != null) {
            if (!this.world.isRemote) {
                if(!done) {//no point receiving if done
                    receiveFromAdjacent();
                    receiveFromAdjacentRF();
                }
                if (blockDigger == null) {
                    blockDigger = (BlockDigger) world.getBlockState(pos).getBlock();
                }

                EnumFacing facing = blockDigger.getFacing();

                if(blockDigger.isPowered(world, pos)) {
                    if (canHarvest()) {
                        if (cooldown == 0) {
                            if (facing == EnumFacing.NORTH) {//z decreases
                                BlockPos nextPos = new BlockPos(pos.getX() + col, pos.getY() - why, pos.getZ() - moved);
                                harvestNext(nextPos);
                            }
                            if (facing == EnumFacing.SOUTH) {//z increases
                                BlockPos nextPos = new BlockPos(pos.getX() - col, pos.getY() - why, pos.getZ() + moved);
                                harvestNext(nextPos);
                            }
                            if (facing == EnumFacing.WEST) {//x is reduced
                                BlockPos nextPos = new BlockPos(pos.getX() - moved, pos.getY() - why, pos.getZ() - col);
                                harvestNext(nextPos);
                            }

                            if (facing == EnumFacing.EAST) {//x is increased
                                BlockPos nextPos = new BlockPos(pos.getX() + moved, pos.getY() - why, pos.getZ() + col);
                                harvestNext(nextPos);
                            }
                            cooldown = maxCooldown;
                        } else {
                            cooldown--;
                        }
                    }
                }else{
                    errormsg = "No Redstone Signal";
                }
            }
        }
    }

    private boolean giveItemToContainer(ItemStack itemStack, int slot) {
        ItemStack result;
        if (invHandler != null) {
            result = invHandler.insertItem(slot, itemStack.copy(), false);
            if (result.isEmpty()) {
                return true;
            } else {
                int nextSlot = slot + 1;
                if (nextSlot < invHandler.getSlots()) {
                    return giveItemToContainer(result, nextSlot);
                } else {
                    return false;
                }

            }
        }
        return false;
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
        why = compound.getInteger("why");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("moved", moved);
        compound.setInteger("col", col);
        compound.setInteger("why", why);
        return compound;
    }

    @Override
    public JsonObject getPacketData() {
        JsonObject superJo = super.getPacketData();
        JsonObject errors = new JsonObject();
        errors.addProperty("errormsg", errormsg);
        superJo.add("errors", errors);
        return superJo;
    }
}
