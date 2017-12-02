/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.tileentities;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.energy.EnergyTransfer;
import com.vengalism.vengamodmc.util.Enums;
import com.vengalism.vengamodmc.util.ItemTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class TileEntityEnergyFurnace extends TileEntityFurnace {

    private static final int BATSLOT = 2;

    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime, count1, count2, count3;
    private int totalCookTime = 125;
    private int sync = 0;
    private ItemStackHandler invHandler;
    private CustomForgeEnergyStorage storage;
    private Enums.MACHINETIER machinetier;

    public TileEntityEnergyFurnace() {
        this(Enums.MACHINETIER.ONE);
    }

    public TileEntityEnergyFurnace(Enums.MACHINETIER machinetier){
        this.storage = new CustomForgeEnergyStorage(Config.energyFurnaceMaxEnergyStored, Config.energyFurnaceEnergyReceiveSpeed, 0);
        this.machinetier = machinetier;
        updateMachineTier();
    }

    private void updateMachineTier(){
        switch (machinetier){
            case ONE:
                this.invHandler = new ItemStackHandler(3);
                break;
            case TWO:
                this.invHandler = new ItemStackHandler(5);
                break;
            case THREE:
                this.invHandler = new ItemStackHandler(7);
                break;
            default:
                this.invHandler = new ItemStackHandler(3);
                break;
        }
    }

    public boolean canExtract(ItemStack stack, int slot){
        return slot == 1 || slot == 4 || slot == 6;
    }
/*
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }*/

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this.storage;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.invHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean isBurning() {
        return this.storage.getEnergyStored() > 0;
    }

    private int cookThese(ItemStack inputItem, ItemStack outputItem, int countTime, int slotNum) {
        if (this.isBurning() || !outputItem.isEmpty() && !inputItem.isEmpty()) {
            if (this.canSmelt(inputItem, outputItem)) {
                if (this.storage.getEnergyStored() > 0) {
                    //as long as we have energy we have buruntime to cook
                    this.furnaceBurnTime = this.storage.getEnergyStored();
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (inputItem.getCount() > 0) {
                        this.storage.extractInternalEnergy(Config.energyFurnaceSmeltUpkeep, false); //upkeep for burning
                    }

                    if (this.isBurning()) {
                        if (!outputItem.isEmpty()) {
                            Item item = outputItem.getItem();
                            if (outputItem.isEmpty()) {
                                //this.furnaceItemStacks.set(1, item5);
                                outputItem = item.getContainerItem(outputItem);
                            }
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt(inputItem, outputItem)) {
                if (countTime == Config.energyFurnaceCookSpeed) {
                    countTime = 0;
                    this.totalCookTime = this.getCookTime(inputItem);
                    this.smeltItem(inputItem, outputItem, slotNum);
                }
            } else {
                countTime = 0;
            }
        } else if (!this.isBurning() && countTime > 0) {
            countTime = MathHelper.clamp(countTime - 2, 0, this.totalCookTime);
        }

        return countTime;
    }

    @Override
    public void update() {
        if (this.world != null) {

            if (!this.world.isRemote) {
                sync++;
                sync %= 20;
                if (sync == 0) {

                    //take from adjacent blocks
                    int maxCanReceive = this.storage.getMaxCanReceive();
                    this.storage.receiveInternalEnergy(EnergyTransfer.takeAll(this.world, this.pos, maxCanReceive, false), false);



                    //take from battery in slot 2
                    ItemStack item2 = this.invHandler.getStackInSlot(BATSLOT);
                    if (item2.getCount() > 0) {
                        maxCanReceive = this.storage.getMaxCanReceive();
                        if (maxCanReceive > 0) {
                            this.storage.receiveEnergy(EnergyTransfer.takeEnergyFromItem(item2, maxCanReceive, false, null), false);
                        }
                    }
                    switch (machinetier){
                        case THREE:
                            //tier 3
                            ItemStack input3 = this.invHandler.getStackInSlot(5);
                            ItemStack output3 = this.invHandler.getStackInSlot(6);
                            ++this.count3;
                            count3 = cookThese(input3, output3, count3, 6);
                            if (output3.getCount() > 0) {
                                exportOutput(output3);
                            }
                        case TWO:
                            //tier 2
                            ItemStack input2 = this.invHandler.getStackInSlot(3);
                            ItemStack output2 = this.invHandler.getStackInSlot(4);
                            ++this.count2;
                            count2 = cookThese(input2, output2, count2, 4);
                            if (output2.getCount() > 0) {
                                exportOutput(output2);
                            }
                        case ONE:
                            //tier 1
                            ItemStack input1 = this.invHandler.getStackInSlot(0);
                            ItemStack output1 = this.invHandler.getStackInSlot(1);
                            ++this.count1;
                            count1 = cookThese(input1, output1, count1, 1);
                            if (output1.getCount() > 0) {
                                exportOutput(output1);
                            }
                            break;
                        default:
                            break;
                    }



                }
            }

        }
    }



    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1 || index == 4 || index == 6;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 || index == 3 || index == 5;
    }

    private void exportOutput(ItemStack output){//TODO fix this, exports to everything
        if(Config.energyFurnaceAutoExport) {
            ArrayList<TileEntity> adjacentBlocks = ItemTransfer.getTiles(this.world, this.pos, EnumFacing.NORTH);
            for (TileEntity te : adjacentBlocks) {
                IItemHandler to = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
                if (to != null) {
                    ItemTransfer.giveItemToAdjacent(output, to);
                }
            }
        }
    }

    private boolean canSmelt(ItemStack input, ItemStack output) {

        if (input.isEmpty()) {
            return false;
        } else {
            ItemStack recipeResult = FurnaceRecipes.instance().getSmeltingResult(input);

            if (recipeResult.isEmpty()) {
                return false;
            } else {

                if (output.isEmpty()) {
                    return true;
                } else if (!output.isItemEqual(recipeResult)) {
                    return false;
                    // Forge fix: make furnace respect stack sizes in furnace recipes
                } else if (output.getCount() + recipeResult.getCount() <= this.getInventoryStackLimit() && output.getCount() + recipeResult.getCount() <= output.getMaxStackSize()) {
                    return true;
                } else {
                    return output.getCount() + recipeResult.getCount() <= recipeResult.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    private void smeltItem(ItemStack input, ItemStack output, int outputSlotNum) {
        ItemStack recipeResult = FurnaceRecipes.instance().getSmeltingResult(input);

        if (output.isEmpty()) {
            //output = recipeResult.copy();
            this.invHandler.setStackInSlot(outputSlotNum, recipeResult.copy());
        } else if (output.getItem() == recipeResult.getItem()) {
            output.grow(recipeResult.getCount());
        }

        input.shrink(1);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        ItemStack itemstack = this.invHandler.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.invHandler.setStackInSlot(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            //this.totalCookTime = this.getCookTime(stack);
            this.totalCookTime = 125;
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public Enums.MACHINETIER getMachinetier() {
        return this.machinetier;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.furnaceBurnTime = compound.getInteger("furnaceBurnTime");
        this.currentItemBurnTime = compound.getInteger("currentItemBurnTime");
        this.cookTime = compound.getInteger("cookTime");
        this.count1 = compound.getInteger("count1");
        this.count2 = compound.getInteger("count2");
        this.count3 = compound.getInteger("count3");
        this.storage.readFromNBT(compound);
        this.invHandler.deserializeNBT(compound.getCompoundTag("genInv"));
        String tierName = compound.getString("machTier");
        this.machinetier = Enums.MACHINETIER.valueOf(tierName);
        updateMachineTier();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("furnaceBurnTime", furnaceBurnTime);
        compound.setInteger("currentItemBurnTime", currentItemBurnTime);
        compound.setInteger("cookTime", cookTime);
        compound.setInteger("count1", count1);
        compound.setInteger("count2", count2);
        compound.setInteger("count3", count3);
        this.storage.writeToNBT(compound);
        compound.setTag("genInv", this.invHandler.serializeNBT());
        compound.setString("machTier", this.machinetier.getName());
        return compound;
    }
}
