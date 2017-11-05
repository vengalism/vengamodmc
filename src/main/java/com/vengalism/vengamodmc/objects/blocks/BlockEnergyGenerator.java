/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.blocks;


import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyGenerator;
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
 * Created by vengada on 18/07/2017.
 */
public class BlockEnergyGenerator extends BlockBase implements ITileEntityProvider {

    private Enums.MACHINETIER machinetier;

    public BlockEnergyGenerator(String name){
        this(name, Enums.MACHINETIER.ONE);
    }

    public BlockEnergyGenerator(String name, Enums.MACHINETIER machinetier) {
        this(name, Material.IRON,machinetier);
    }

    public BlockEnergyGenerator(String name, Material material, Enums.MACHINETIER machinetier){
        super(name, material);
        this.machinetier = machinetier;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyGenerator(machinetier);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityEnergyGenerator)){
            return false;
        }

        playerIn.openGui(VengaModMc.instance, GuiHandler.energyGeneratorContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
