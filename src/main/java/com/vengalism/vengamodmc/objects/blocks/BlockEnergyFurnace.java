/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyFurnace;
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

public class BlockEnergyFurnace extends BlockBase implements ITileEntityProvider {

    private static boolean isBurning = false;
    private Enums.MACHINETIER machinetier;

    public BlockEnergyFurnace(){
        this("block_energy_furnace", false, Enums.MACHINETIER.ONE);
    }
    public BlockEnergyFurnace(String name, boolean isBurning){
        this(name, isBurning, Enums.MACHINETIER.ONE);
    }

    public BlockEnergyFurnace(String name, boolean isBurning, Enums.MACHINETIER machinetier) {
        super(name, Material.IRON);
        BlockEnergyFurnace.isBurning = isBurning;
        this.machinetier = machinetier;
    }



    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyFurnace(this.machinetier);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityEnergyFurnace)){
            return false;
        }

        playerIn.openGui(VengaModMc.instance, GuiHandler.energyFurnaceContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
