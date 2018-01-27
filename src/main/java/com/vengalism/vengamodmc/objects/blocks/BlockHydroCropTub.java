package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroCropTub;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

/**
 * Created by vengada at 22/10/2017
 */
public class BlockHydroCropTub extends BlockBase{

    private static final PropertyInteger FLUIDLEVEL = PropertyInteger.create("fluidlevel", 0, 3);
    private int level = 0;

    public BlockHydroCropTub(String name) {
        super(name, Material.IRON);
        //this.setDefaultState(this.getDefaultState().withProperty(FLUIDLEVEL, 0));
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

        if(playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
            requireRedstone = !requireRedstone;
            playerIn.sendMessage(new TextComponentTranslation("Requires Redstone: " + requireRedstone));
            return true;
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


    //TODO multiple properties for blocks, something like this, or further research into statemappers


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FLUIDLEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FLUIDLEVEL, level);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FLUIDLEVEL);
    }

    public void setFillLevel(int level){
        this.level = level;
    }

}
