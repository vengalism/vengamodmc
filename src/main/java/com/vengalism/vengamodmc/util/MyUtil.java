/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class MyUtil {

    public static ArrayList<TileEntity> getAdjacentBlocks(World world, BlockPos pos, Boolean horiz, Boolean vert){
        ArrayList<TileEntity> tilesToCheck = new ArrayList<>();
        ArrayList<TileEntity> cleanedTilesToCheck = new ArrayList<>();
        tilesToCheck.clear();
        if(world != null && !world.isRemote) {
            BlockPos neigh;

            if(horiz){
                for(EnumFacing facing : EnumFacing.HORIZONTALS){
                    neigh = pos.offset(facing);
                    tilesToCheck.add(world.getTileEntity(neigh));
                }
            }

            if(vert){
                for(EnumFacing facing : EnumFacing.VALUES){
                    neigh = pos.offset(facing);
                    tilesToCheck.add(world.getTileEntity(neigh));
                }
            }

            for(TileEntity te : tilesToCheck){
                if(!(cleanedTilesToCheck.contains(te) && te != null)){
                    cleanedTilesToCheck.add(te);
                }
            }
        }
        return cleanedTilesToCheck;
    }

    public static ArrayList<TileEntity> getAdjacentBlocks(World world, BlockPos pos){
        return getAdjacentBlocks(world, pos, true, true);
    }
}
