/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerEnergyStorage;
import com.vengalism.vengamodmc.energy.EnergyBar;
import com.vengalism.vengamodmc.network.PacketGetEnergy;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by vengada on 16/07/2017.
 */
public class GUIEnergyStorage extends CustomEnergyGuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/container2.png");
    public static int energy, maxEnergy;
    private static int sync = 0;
    private TileEntityEnergyStorage tileEntityEnergyStorage;
    private EnergyBar energyBar;

    public GUIEnergyStorage(InventoryPlayer player, TileEntityEnergyStorage tileEntityEnergyStorage) {
        super(new ContainerEnergyStorage(player, tileEntityEnergyStorage));
        this.tileEntityEnergyStorage = tileEntityEnergyStorage;
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
        fontRenderer.drawString(new TextComponentTranslation("Basic Energy Vault").getFormattedText(), 5, 5, Color.darkGray.getRGB());
        sync++;
        sync %= 20;
        if (sync == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetEnergy(this.tileEntityEnergyStorage.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIEnergyStorage", "energy", "maxEnergy"));
        }
        energyBar.updateEnergyBar(energy, maxEnergy);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
