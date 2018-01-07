/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class MyUtil {

    public static ArrayList<TileEntity> getAdjacentBlocks(World world, BlockPos pos, Boolean horiz, Boolean vert){
        ArrayList<TileEntity> tilesToCheck = new ArrayList<>();
        ArrayList<TileEntity> cleanedTilesToCheck = new ArrayList<>();
        tilesToCheck.clear();
        if(world != null && !world.isRemote) {
            //System.out.println("ET world not null");
            if(horiz){
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())));
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())));
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)));
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)));
            }

            if(vert){
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())));
                tilesToCheck.add(world.getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())));
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
