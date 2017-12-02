/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.blocks;

import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.util.BlockUtil;
import com.vengalism.vengamodmc.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockBase extends Block implements IHasModel, ITileEntityProvider{

    private boolean keepInventory = false;


    public BlockBase(String name, Material material) {
        this(name, material, true);
    }

    //Block block, Item itemBlock, String name, boolean addTab
    public BlockBase(String name, Material material, boolean addTab){
        super(material);
        setHardness(5.0f);
        setHarvestLevel("pickaxe", 2);
        BlockUtil.registerBlock(this, getItemBlock(this), name, addTab);

    }

    private Item getItemBlock(Block block){
        return new ItemBlock(block);
    }

    @Override
    public void registerModels() {
        VengaModMc.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            IItemHandler invHandler;
            if (tileentity != null) {
                invHandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if(invHandler != null){
                    for(int i = 0; i < invHandler.getSlots(); i++){
                        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY() +1, pos.getZ(), invHandler.getStackInSlot(i));
                    }
                    worldIn.removeTileEntity(pos);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
