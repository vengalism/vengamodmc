package com.vengalism.vengamodmc.tileentities;

import com.google.gson.JsonObject;
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

    //private TileEntityDiggerController controller;
    private int cooldown = 20; //ticks
    private static final int maxCooldown = 20;
    private static final int ENERGYPERUSE = 100;
    private int moved = 1, maxMove = 16, col = 0, why = 0;
    private ItemStackHandler invHandler;
    private final int WHY = 1;
    private BlockPos lastExcavatedPos;
    private BlockDigger blockDigger;
    private BlockPos startPos;
    private boolean done = false;
    private String errormsg = "";

    public TileEntityDigger() {
        this(10000);
    }

    public TileEntityDigger(int capacity) {
        super(capacity);
        this.invHandler = new ItemStackHandler(24);
        this.storage.canReceive();
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
            //System.out.println("not done");
            if (this.storage.getEnergyStored() > ENERGYPERUSE) {
                //System.out.println("has energy");
                if (hasEmptyInvSlot()) {
                    //System.out.println("has slots");
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
        System.out.println(nextPos + " next pos");
        Block block = world.getBlockState(nextPos).getBlock();
        if ((block != Blocks.AIR) && (block != Blocks.BEDROCK)) {
            giveItemToController(new ItemStack(Item.getItemFromBlock(block), 1), 0);
            //block.breakBlock(world, nextPos, block.getDefaultState());
            this.storage.extractEnergy(ENERGYPERUSE, false);
            world.setBlockState(nextPos, Blocks.AIR.getDefaultState());
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
                }
            }
        }
    }

    @Override
    public void update() {
        if (this.world != null) {
            if (!this.world.isRemote) {
                receiveFromAdjacent();
                receiveFromAdjacentRF();
                if (blockDigger == null) {
                    blockDigger = (BlockDigger) world.getBlockState(pos).getBlock();
                }


                if (startPos == null) {
                    startPos = this.pos;
                }

                EnumFacing facing = blockDigger.getFacing();

                if (canHarvest()) {
                    //System.out.println(cooldown + " cooldown");
                    if (cooldown == 0) {
                        //System.out.println(facing + " facing");

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
            }
        }
    }

    private boolean giveItemToController(ItemStack itemStack, int slot) {
        ItemStack result = ItemStack.EMPTY;
        if (invHandler != null) {
            result = invHandler.insertItem(slot, itemStack.copy(), false);
            if (result.isEmpty()) {
                return true;
            } else {
                int nextSlot = slot + 1;
                System.out.println(nextSlot + " next slot, slot: " + slot);
                System.out.println(result.getItem().getUnlocalizedName() + " count " + result.getCount() + " result");
                if (nextSlot < invHandler.getSlots()) {
                    return giveItemToController(result, nextSlot);
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
