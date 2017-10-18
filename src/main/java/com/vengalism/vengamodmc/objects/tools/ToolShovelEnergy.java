/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class ToolShovelEnergy extends ToolEnergy {

    private static final int ENERGYPERUSE = 100;
    public static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND,
            Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND,
            Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);

    public ToolShovelEnergy(String name, ToolMaterial materialIn){
        this(name, 1.5F, -3.0F, materialIn, EFFECTIVE_ON, true);
    }

    public ToolShovelEnergy(String name, float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn, boolean defaultEnergyStorage) {
        super(name, attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn, defaultEnergyStorage);
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(IBlockState blockIn) {

        Block block = blockIn.getBlock();
        return block == Blocks.SNOW_LAYER || block == Blocks.SNOW;
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack itemstack = player.getHeldItem(hand);

        if(this.getEnergyStored(itemstack) > ENERGYPERUSE) {
            if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
                    IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                    worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    if (!worldIn.isRemote) {
                        worldIn.setBlockState(pos, iblockstate1, 11);
                        itemstack.damageItem(1, player);
                    }
                    this.extractInternalEnergy(itemstack, ENERGYPERUSE, false);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(this.getEnergyStored(stack) > ENERGYPERUSE) {
            System.out.println("Shovel destroy ");
            this.extractInternalEnergy(stack, ENERGYPERUSE, false);
            return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        }
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return 2;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return Sets.newHashSet("shovel");
    }
}
