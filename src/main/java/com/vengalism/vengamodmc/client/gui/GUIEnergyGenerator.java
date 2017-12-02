/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.client.gui;

import com.google.gson.JsonObject;
import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerEnergyGenerator;
import com.vengalism.vengamodmc.container.ContainerEnergyStorage;
import com.vengalism.vengamodmc.energy.EnergyBar;
import com.vengalism.vengamodmc.network.PacketGetData;
import com.vengalism.vengamodmc.network.PacketGetEnergy;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by vengada on 18/07/2017.
 */
public class GUIEnergyGenerator extends CustomEnergyGuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/energygeneratorgui.png");
    public static int energy = 0, maxEnergy = 0;
    public static JsonObject data = new JsonObject();
    private static int sync = 0;
    private TileEntityEnergyGenerator generatorTileEntity;
    private EnergyBar energyBar;

    public GUIEnergyGenerator(InventoryPlayer player, TileEntityEnergyGenerator generatorTileEntity) {
        super(new ContainerEnergyGenerator(player, generatorTileEntity));
        this.generatorTileEntity = generatorTileEntity;
        this.xSize = 176;
        this.ySize = 166;
        this.ebx = this.width /2 - this.xSize /2;
        this.eby = this.height /2 - this.ySize/2;
        data.addProperty("valid", false);
    }

    @Override
    public void initGui() {
        this.energyBar = getDefaultEnergyBar();
        this.buttonList.add(energyBar);
        super.initGui();
    }

    @Override
    public void displayPacketInfo() {
        if(data.has("valid")){
            if(data.get("valid").getAsBoolean()){
                JsonObject energyStorage = data.getAsJsonObject("energyStorage");
                if(this.energyBar.isMouseOver()){
                    this.drawHoveringText(energyStorage.get("energy") + " / " + energyStorage.get("maxEnergy"), ebx, eby);
                }
                JsonObject blockInfo = data.getAsJsonObject("blockInfo");
                fontRenderer.drawString(new TextComponentTranslation(blockInfo.get("name").toString()).getFormattedText(), 5, 5, Color.darkGray.getRGB());


            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        displayPacketInfo();

        sync++;
        sync %= 20;
        if (sync == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetData(this.generatorTileEntity.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIEnergyGenerator", "data"));

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
