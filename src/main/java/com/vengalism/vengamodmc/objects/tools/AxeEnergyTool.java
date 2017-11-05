/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.google.common.collect.Sets;
import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class AxeEnergyTool extends ToolEnergy{

    public static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
    private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

    private static final int ENERGYPERUSE = Config.axeEnergyPerUse;

    public AxeEnergyTool(String name, ToolMaterial materialIn){
        this(name, materialIn, EFFECTIVE_ON);
    }

    public AxeEnergyTool(String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(name, materialIn, effectiveBlocksIn);
        this.attackDamage = ATTACK_DAMAGES[materialIn.ordinal()];
        this.attackSpeed = ATTACK_SPEEDS[materialIn.ordinal()];
        this.setHarvestLevel("axe", materialIn.getHarvestLevel());

    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if( this.getEnergyStored(itemstack)> ENERGYPERUSE){
            return super.onBlockStartBreak(itemstack, pos, player);
        }
        return false;

    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(this.getEnergyStored(stack) > ENERGYPERUSE) {
            this.extractInternalEnergy(stack, ENERGYPERUSE, false);
            return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        }
        return false;
    }

    /*
    * @param level Harvest level:
     *     Wood:    0
     *     Stone:   1
     *     Iron:    2
     *     Diamond: 3
     *     Gold:    0
     */
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return 2;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return Sets.newHashSet("axe");
    }


}
