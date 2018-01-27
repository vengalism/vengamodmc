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
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by vengada at 15/10/2017
 */
public class BlockEnergyStorage extends BlockBase implements ITileEntityProvider {

    private Enums.MACHINETIER machinetier;
    private static final PropertyDirection FACING = PropertyDirection.create("facing");
    private static final PropertyInteger FILL = PropertyInteger.create("fillamount", 0, 10);
    private EnumFacing faceAtPlace = EnumFacing.NORTH;
    private int percentFull = 0;

    public BlockEnergyStorage(String name) {
        this(name, Enums.MACHINETIER.ONE);
    }

    public BlockEnergyStorage(String name, Enums.MACHINETIER machinetier) {
        super(name, Material.IRON);
        this.machinetier = machinetier;
        this.setDefaultState(blockState.getBaseState().withProperty(FILL, 0).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        this.faceAtPlace = getFacingFromEntity(pos, placer);
    }

    public void updateState(World world, BlockPos pos, int percentFull){
        this.percentFull = percentFull;
        world.setBlockState(pos, world.getBlockState(pos).withProperty(FILL, percentFull));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnergyStorage(this.machinetier);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityEnergyStorage)) {
            return false;
        }

        playerIn.openGui(VengaModMc.instance, GuiHandler.energyStorageContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FILL, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FILL, this.percentFull).withProperty(FACING, EnumFacing.getFront(faceAtPlace.getIndex()));
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FILL)) + state.getValue(FACING).getIndex();
        /*int i = 0;

        i |= (state.getValue(FACING)).getIndex();
        i |= (state.getValue(FILL) < 10) ? state.getValue(FILL) : percentFull;

        return i;*/
    }


}
