/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyStorage;
import com.vengalism.vengamodmc.util.Enums;
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
 * Created by vengada at 15/10/2017
 */
public class BlockEnergyStorage extends BlockBase implements ITileEntityProvider{

    private Enums.MACHINETIER machinetier;

    public BlockEnergyStorage(String name) {
        this(name, Enums.MACHINETIER.ONE);
    }

    public BlockEnergyStorage(String name, Enums.MACHINETIER machinetier){
        super(name, Material.IRON);
        this.machinetier = machinetier;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyStorage(this.machinetier);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityEnergyStorage)){
            return false;
        }

        playerIn.openGui(VengaModMc.instance, GuiHandler.energyStorageContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }


}
