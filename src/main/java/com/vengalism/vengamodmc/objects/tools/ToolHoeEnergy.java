/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.google.common.collect.Sets;
import com.vengalism.vengamodmc.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class ToolHoeEnergy extends ToolEnergy {
    private static final Set<Block> EMPTYSET = Sets.newHashSet(Blocks.AIR);

    private static final int ENERGYPERUSE = Config.hoeEnergyPerUse;

    public ToolHoeEnergy(String name, ToolMaterial materialIn){
        this(name, materialIn, EMPTYSET);
    }

    public ToolHoeEnergy(String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(name, materialIn, effectiveBlocksIn);
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state){
        if(this.getEnergyStored(stack) > ENERGYPERUSE) {
            worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state, 11);
                this.extractInternalEnergy(stack, ENERGYPERUSE, false);
            }
        }
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @SuppressWarnings("incomplete-switch")
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)){
            return EnumActionResult.FAIL;
        }else{
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
            if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())){
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH){
                    this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT){
                    switch (iblockstate.getValue(BlockDirt.VARIANT)){
                        case DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return 2;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        //return Stream.of("hoe").collect(Collectors.toSet());
        return Sets.newHashSet("hoe");
    }



}
