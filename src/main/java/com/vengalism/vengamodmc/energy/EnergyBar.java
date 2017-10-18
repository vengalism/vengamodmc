/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.energy;

import com.vengalism.vengamodmc.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class EnergyBar extends GuiButton {

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/energy_bar.png");
    private static final int DEFAULT_WIDTH = 18;
    private static final int DEFAULT_HEIGHT = 84;
    private int textureX;
    private int textureY;
    private int color = 0;
    public int energy;
    private int maxEnergy;

    public EnergyBar(int buttonId, int x, int y, int energy, int maxEnergy) {
        super(buttonId, x, y, "");
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.textureX = 0;
        this.textureY = 0;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public EnergyBar(int buttonId, int x, int y, int width, int height, int energy, int maxEnergy) {
        super(buttonId, x, y, "");
        this.width = width >= DEFAULT_WIDTH ? DEFAULT_WIDTH : width;
        this.height = height >= DEFAULT_HEIGHT ? DEFAULT_HEIGHT : height;
        this.textureX = 0;
        this.textureY = 0;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                && mouseY < this.y + this.height;

        // Outer rim of bar
        this.drawVerticalLine(x, y - 1, y + height, 0xFF373737);
        this.drawHorizontalLine(x + 1, x + width - 2, y, 0xFF373737);
        this.drawHorizontalLine(x + width - 1, x + width - 1, y, 0xFF8B8B8B);
        this.drawHorizontalLine(x, x, y + height, 0xFF8B8B8B);
        this.drawVerticalLine(x + width - 1, y, y + height, 0xFFFFFFFF);
        this.drawHorizontalLine(x + 1, x + width - 1, y + height, 0xFFE2E2E2);

        // Actual background energy bar
        mc.getTextureManager().bindTexture(DEFAULT_TEXTURE);
        int colour1 = Color.BLUE.getRGB();
        int colour2 = Color.GREEN.getRGB();
        int colour3 = Color.WHITE.getRGB();
        GlStateManager.color(colour1, colour2, colour3);
        this.drawTexturedModalRect(x + 1, y + 1, textureX + 1 + (Math.abs(DEFAULT_WIDTH - width) / 2),
                textureY + 1, width - 2, height - 1);

        // The overlay to show the amount of energy in the {@link TileEntity}
        mc.getTextureManager().bindTexture(DEFAULT_TEXTURE);
        colorType();

        this.drawTexturedModalRect(x + 1, y + 1, textureX + 1 + (Math.abs(DEFAULT_WIDTH - width) / 2),
                textureY + 1, width - 2, height - getEnergyBarHeight() - 1);

    }

    public void updateEnergyBar(int energy, int maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    private int getEnergyBarHeight() {
        return ((this.maxEnergy != 0 && this.energy != 0) ? (this.energy * this.height) / this.maxEnergy : 0);
    }

    public void setColor(int color){
        this.color = color;
    }

    private void colorType(){
        switch (color){
            case 0 : GlStateManager.color(1.0F, 1.0F, 1.0F);
                break;
            case 1 : GlStateManager.color(0.5F, 1.0F, 0.5F);
                break;
            default:
                GlStateManager.color(0.0F, 1.0F, 0.0F);
        }
    }
}
