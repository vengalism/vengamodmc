/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.objects.tools;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.VengaModMc;
import com.vengalism.vengamodmc.util.IHasModel;
import com.vengalism.vengamodmc.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

public class ToolBase extends ItemTool implements IHasModel {

    private String name;

    protected ToolBase(String name, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
        this.name = name;
        this.register();
    }

    protected ToolBase(String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(materialIn, effectiveBlocksIn);
        this.name = name;
        this.register();
    }

    private void register(){
        ItemUtil.registerItem(this, this.name, true);

        this.registerRendering();
    }

    protected void registerRendering(){
        VengaModMc.proxy.addRenderRegister(new ItemStack(this), new ModelResourceLocation(Reference.MODID + ":" +this.getUnlocalizedName().substring(5)), "inventory");
    }

    @Override
    public boolean getShareTag() {

        return true;
    }

    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public void registerModels() {
        VengaModMc.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
