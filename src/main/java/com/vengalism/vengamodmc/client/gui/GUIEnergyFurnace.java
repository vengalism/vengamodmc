/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerEnergyFurnace;
import com.vengalism.vengamodmc.energy.CustomForgeEnergyStorage;
import com.vengalism.vengamodmc.network.PacketGetEnergy;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

public class GUIEnergyFurnace extends CustomEnergyGuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/energyfurnacegui.png");
    public static int energy = 0, maxEnergy = 0;
    private static int sync = 0;
    private int storage = 0;
    private CustomForgeEnergyStorage customForgeEnergyStorage;
    private TileEntityEnergyFurnace tileEntityEnergyFurnace;

    public GUIEnergyFurnace(InventoryPlayer player, TileEntityEnergyFurnace tileEntityEnergyFurnace) {
        super(new ContainerEnergyFurnace(player, tileEntityEnergyFurnace));
        this.tileEntityEnergyFurnace = tileEntityEnergyFurnace;
        xSize = 176;
        ySize = 166;
    }

    @Override
    public void initGui() {
        this.energyBar = getDefaultEnergyBar();
        this.buttonList.add(energyBar);
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        if(this.energyBar.isMouseOver()){
            this.drawHoveringText(energy + " / " + maxEnergy, ebx, eby);
        }

        fontRenderer.drawString(new TextComponentTranslation("Vault Furnace").getFormattedText(), 5, 5, Color.darkGray.getRGB());
        sync++;
        sync %= 20;
        if (sync == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetEnergy(this.tileEntityEnergyFurnace.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIEnergyFurnace", "energy", "maxEnergy"));
            this.energyBar.updateEnergyBar(energy, maxEnergy);
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
