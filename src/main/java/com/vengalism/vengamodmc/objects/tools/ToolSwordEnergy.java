/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.google.common.collect.Sets;
import com.vengalism.vengamodmc.Config;
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

public class ToolSwordEnergy extends ToolEnergy {

    private static final int ENERGYPERUSE = Config.swordEnergyPerUse;
    private static final Set<Block> EMPTYSET = Sets.newHashSet(Blocks.AIR);

    public ToolSwordEnergy(String name, ToolMaterial materialIn){
        this(name, materialIn, EMPTYSET);
    }

    public ToolSwordEnergy(String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(name, materialIn, effectiveBlocksIn);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
        if(this.getEnergyStored(stack) > ENERGYPERUSE) {
            stack.damageItem(0, attacker);
            this.extractInternalEnergy(stack, ENERGYPERUSE, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        return blockIn.getBlock() == Blocks.WEB;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving){
        if(this.getEnergyStored(stack) > ENERGYPERUSE) {
            if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
                stack.damageItem(0, entityLiving);
                this.extractInternalEnergy(stack, ENERGYPERUSE, false);
            }

            return true;
        }
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return 2;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return Sets.newHashSet("sword");
    }
}
