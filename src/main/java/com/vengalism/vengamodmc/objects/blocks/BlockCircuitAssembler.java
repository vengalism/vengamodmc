/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityCircuitAssembler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by vengada at 18/10/2017
 */
public class BlockCircuitAssembler extends BlockBase implements ITileEntityProvider {

    public BlockCircuitAssembler(String name, Material material) {
        super(name, material);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCircuitAssembler();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityCircuitAssembler)){
            return false;
        }

        //TODO update gui for this block
        playerIn.openGui(VengaModMc.instance, GuiHandler.energyGeneratorContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
