package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroCropTub;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * Created by vengada at 22/10/2017
 */
public class BlockHydroCropTub extends BlockBase{

    private static final PropertyInteger FLUIDLEVEL = PropertyInteger.create("fluidlevel", 0, 3);

    public BlockHydroCropTub(String name) {
        super(name, Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FLUIDLEVEL, 0));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHydroCropTub();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityHydroCropTub)){
            return false;
        }

        //TODO update gui for this block
        playerIn.openGui(VengaModMc.instance, GuiHandler.hydroTubContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        if(!world.isRemote){
            TileEntity te = world.getTileEntity(pos);
            if(te != null) {
                if (te instanceof TileEntityHydroCropTub) {
                    TileEntityHydroCropTub tub = (TileEntityHydroCropTub) te;
                    return tub.getFluidTank().getFluidAmount() > 0;
                }
            }
        }
        return false;

    }


    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FLUIDLEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FLUIDLEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FLUIDLEVEL);
    }

}
