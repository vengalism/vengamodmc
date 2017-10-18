/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.energy;

import com.vengalism.vengamodmc.util.MyUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vengada on 16/07/2017.
 */
public class EnergyTransfer {

    private static int giveEnergy(TileEntity te, int energy, boolean simulate, EnumFacing from) {

        if (te.hasCapability(CapabilityEnergy.ENERGY, from)) {
            IEnergyStorage ies = te.getCapability(CapabilityEnergy.ENERGY, null);
            if(ies != null && ies.canReceive()){
                return ies.receiveEnergy(energy, simulate);
            }
        }
        return 0;
    }

    private static int takeEnergy(TileEntity te, int energy, boolean simulate, EnumFacing from) {
        if (te.hasCapability(CapabilityEnergy.ENERGY, from)) {
            IEnergyStorage ies = te.getCapability(CapabilityEnergy.ENERGY, null);
            if(ies != null && ies.canExtract()){
                return ies.extractEnergy(energy, simulate);
            }
        }
        return 0;
    }

    public static int takeAll(World world, BlockPos pos, int energy, boolean simulate) {
        ArrayList<TileEntity> energeticTiles = getTiles(world, pos, false);
        if(energeticTiles.size() > 0){
            int energyPerSide = energy / energeticTiles.size();
            int energyTaken = 0;
            int extraEnergy = 0;
            for(TileEntity tileEntity : energeticTiles){
                int et = takeEnergy(tileEntity, energyPerSide + extraEnergy, simulate, EnumFacing.random(new Random()));
                energyTaken += et;
                if (et < energyPerSide) {
                    extraEnergy = energyPerSide - et;
                } else {
                    extraEnergy = 0;
                }
            }
            return energyTaken;
        }else{
            return 0;
        }
    }

    private static ArrayList<TileEntity> getTiles(World world, BlockPos pos, boolean canReceive){
        return getTiles(world, pos, canReceive, false);
    }

    private static ArrayList<TileEntity> getTiles(World world, BlockPos pos, boolean canReceive, boolean RF){
        ArrayList<TileEntity> energyTiles = new ArrayList<>();

        for(TileEntity tileEntity : MyUtil.getAdjacentBlocks(world, pos)){
            if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)) {
                try {
                    IEnergyStorage storage = tileEntity.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH);
                    if (storage != null) {
                        if (canReceive) { //only those that can receive
                            if (storage.canReceive()) {
                                energyTiles.add(tileEntity);
                            }
                        } else { //add only those that can extract
                            if (storage.canExtract()) {
                                energyTiles.add(tileEntity);
                            }
                        }
                    }
                }catch (ClassCastException cce){
                    System.out.println("You Stuffed it " + cce.getMessage());
                }

            }
        }
        return energyTiles;
    }

    public static int giveAll(World world, BlockPos pos, int energy, boolean simulate, boolean multi) {
        ArrayList<TileEntity> energeticTiles;
        //give to multi

        energeticTiles = getTiles(world, pos, true);

        if(energeticTiles.size() > 0){
            int energyPerSide = energy / energeticTiles.size();
            int energyGiven = 0;
            int extraEnergy = 0;
            for(TileEntity tileEntity : energeticTiles){

                int eg = giveEnergy(tileEntity, energyPerSide + extraEnergy, simulate, EnumFacing.random(new Random()));
                energyGiven += eg;
                if (eg < energyPerSide) {
                    extraEnergy = energyPerSide - eg;
                } else {
                    extraEnergy = 0;
                }
            }
            return energyGiven;
        }else{
            return 0;
        }
    }

    public static int giveEnergyToItem(ItemStack itemStack, int energy, boolean simulate, EnumFacing from) {

        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, from)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, from);
            if(storage != null) {
                if(storage instanceof CustomForgeEnergyStorage){
                    CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage)storage;
                    return forgeEnergyStorage.receiveInternalEnergy(itemStack, energy, simulate);
                }else{
                    return storage.receiveEnergy(energy, simulate);
                }
            }
        }
        return 0;
    }

    public static int takeEnergyFromItem(ItemStack itemStack, int energy, boolean simulate, EnumFacing from) {

        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, from)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, from);
            if(storage != null) {
                if(storage instanceof CustomForgeEnergyStorage){
                    CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage)storage;
                    return forgeEnergyStorage.extractInternalEnergy(itemStack, energy, simulate);
                }
                return storage.extractEnergy(energy, simulate);
            }
        }
        return 0;
    }

    public static void setItemEnergy(ItemStack itemStack, int energy, boolean simulate, EnumFacing from){
        if(itemStack.hasCapability(CapabilityEnergy.ENERGY, from)){
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, from);
            if(storage != null) {
                CustomForgeEnergyStorage forgeEnergyStorage = (CustomForgeEnergyStorage)storage;
                forgeEnergyStorage.setEnergy(energy);
            }
        }
    }
}
