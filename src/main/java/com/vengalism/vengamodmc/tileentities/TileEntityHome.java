package com.vengalism.vengamodmc.tileentities;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by vengada at 23/10/2017
 */
public class TileEntityHome extends TileEntityEnergyBase implements ITickable {

    private boolean done = false;

    public TileEntityHome(){
        this(20000);
    }

    public TileEntityHome(int capacity) {
        super(capacity);
    }


    @Override
    public void update() {
        if (this.world != null) {

            if (!this.world.isRemote) {
                if (!done) {
                    World world = this.world;

                    //left wall
                    BlockPos thisPos = this.pos;
                    for(int i = 0; i < 5; i++){
                        for(int y = 1; y < 10; y++){
                            world.setBlockState(new BlockPos(thisPos.getX() + y, thisPos.getY() + i, thisPos.getZ()), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    //rightwall
                    thisPos = new BlockPos(thisPos.getX(), thisPos.getY(), thisPos.getZ() + 8);
                    for(int i = 0; i < 5; i++){
                        for(int y = 1; y < 10; y++){
                            world.setBlockState(new BlockPos(thisPos.getX() + y, thisPos.getY() + i, thisPos.getZ()), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    //backwall
                    thisPos = new BlockPos(this.pos.getX() + 9, this.pos.getY(), this.pos.getZ());
                    for(int i = 0; i < 5; i++){
                        for(int y = 1; y < 9; y++){
                            world.setBlockState(new BlockPos(thisPos.getX(), thisPos.getY() + i, thisPos.getZ() + y), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    //frontwall
                    thisPos = new BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ());
                    for(int i = 0; i < 5; i++){
                        for(int y = 1; y < 9; y++){
                            world.setBlockState(new BlockPos(thisPos.getX(), thisPos.getY() + i, thisPos.getZ() + y), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    //roof
                    thisPos = new BlockPos(this.pos.getX(), this.pos.getY() +4, this.pos.getZ());
                    for(int i = 1; i < 9; i++){
                        for(int y = 1; y < 9; y++){
                            world.setBlockState(new BlockPos(thisPos.getX() + i, thisPos.getY(), thisPos.getZ() + y), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    //floor
                    thisPos = new BlockPos(this.pos.getX() , this.pos.getY(), this.pos.getZ());
                    for(int i = 1; i < 9; i++){
                        for(int y = 1; y < 9; y++){
                            world.setBlockState(new BlockPos(thisPos.getX() + i, thisPos.getY(), thisPos.getZ() + y), Blocks.COBBLESTONE.getDefaultState());
                        }

                    }

                    done = true;
                }
            }
        }
    }
}
