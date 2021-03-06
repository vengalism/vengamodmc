package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.handlers.GuiHandler;
import com.vengalism.vengamodmc.objects.items.ItemMultiTool;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroFishTank;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroNutrientTank;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * Created by vengada at 20/10/2017
 */
public class BlockHydroTank extends BlockBase implements ITileEntityProvider {

    public BlockHydroTank(String name) {
        super(name, Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHydroNutrientTank();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote){
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(!(tileEntity instanceof TileEntityHydroNutrientTank)){
            return false;
        }

        if(playerIn.getHeldItem(hand).getItem() instanceof ItemMultiTool){
            if(playerIn.isSneaking()){
                TileEntity te = worldIn.getTileEntity(pos);
                if(te != null){
                    if(te instanceof TileEntityHydroNutrientTank){
                        TileEntityHydroNutrientTank tank = (TileEntityHydroNutrientTank)te;
                        //tank.getFluidTank().setFluid(new FluidStack(FluidRegistry.WATER, 0));
                        //tank.getNutrientTank().setFluid(new FluidStack(FluidRegistry.WATER, 0));
                        return true;
                    }
                }
            }
        }

        playerIn.openGui(VengaModMc.instance, GuiHandler.hydroTankContainerID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }


}
