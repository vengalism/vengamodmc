package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.tileentities.TileEntityEnergyFloor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by vengada at 3/12/2017
 */
public class BlockEnergyFloor extends BlockBase {

    public BlockEnergyFloor(String name) {
        super(name, Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyFloor(1000);
    }


}
