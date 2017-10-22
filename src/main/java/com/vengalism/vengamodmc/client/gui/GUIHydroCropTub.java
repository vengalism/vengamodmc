package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerHydroCropTub;
import com.vengalism.vengamodmc.container.CustomContainer;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.network.PacketGetFluid;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroCropTub;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by vengada at 22/10/2017
 */
public class GUIHydroCropTub extends CustomEnergyGuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/hydrotubgui.png");
    private int sync = 0;
    public static int fluidAmount = 0, capacity = 0;
    public static int fluidType = 0;
    private TileEntityHydroCropTub tileEntityHydroCropTub;

    public GUIHydroCropTub(InventoryPlayer player, TileEntityHydroCropTub tileEntityHydroCropTub) {
        super(new ContainerHydroCropTub(player, tileEntityHydroCropTub));
        this.tileEntityHydroCropTub = tileEntityHydroCropTub;
        xSize = 176;
        ySize = 166;
    }


    @Override
    public void initGui() {
        this.energyBar = getDefaultEnergyBar();
        this.buttonList.add(this.energyBar);
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String nutType = "Nutrient Fluid";
        if(fluidType != 0){
            nutType = "Oxygenated Nutrient Fluid";
        }

        if(this.energyBar.isMouseOver()){
            this.drawHoveringText(fluidAmount + " / " + capacity + " " + nutType , ebx, eby);
        }

        fontRenderer.drawString(new TextComponentTranslation("Hydro Crop Tub").getFormattedText(), 5, 5, Color.darkGray.getRGB());

        sync++;
        sync %= 20;
        if (sync == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetFluid(this.tileEntityHydroCropTub.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroCropTub", "fluidAmount", "capacity", "fluidType"));
            this.energyBar.updateEnergyBar(fluidAmount, capacity);
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
