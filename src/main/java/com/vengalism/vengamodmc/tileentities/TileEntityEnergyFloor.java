package com.vengalism.vengamodmc.tileentities;

import net.minecraft.util.ITickable;

/**
 * Created by vengada at 3/12/2017
 */
public class TileEntityEnergyFloor extends TileEntityEnergyBase implements ITickable {

    public TileEntityEnergyFloor(int capacity) {
        super(capacity, 500);
    }


    @Override
    public void update() {
        extractToAdjacent();
        receiveFromAdjacent();
    }
}
