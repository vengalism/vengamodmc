package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerHydroTank;
import com.vengalism.vengamodmc.energy.EnergyBar;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.network.PacketGetFluid;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

public class GUIHydroTank extends CustomEnergyGuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/hydro_tank_gui.png");
    private TileEntityHydroTank hydroTankTileEntity;
    public static int waterFluidAmount = 0, waterCapacity = 0;
    public static int nutrientFluidAmount = 0, nutrientCapacity = 0;
    public static int fluidType = 0, waterFluidType = 0;
    private static int sync = 0;
    private EnergyBar NutrientEnergyBar;

    public GUIHydroTank(InventoryPlayer player, TileEntityHydroTank hydroTankTileEntity) {
        super(new ContainerHydroTank(player, hydroTankTileEntity));
        this.hydroTankTileEntity = hydroTankTileEntity;
        xSize = 176;
        ySize = 166;
    }

    @Override
    public void initGui() {
        this.NutrientEnergyBar = getNextToDefaultEnergyBar();
        this.NutrientEnergyBar.setColor(1);
        this.energyBar = getDefaultEnergyBar();
        this.energyBar.setColor(0);
        this.buttonList.add(this.energyBar);
        this.buttonList.add(this.NutrientEnergyBar);
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        if(this.energyBar.isMouseOver()){
            this.drawHoveringText(waterFluidAmount + " / " + waterCapacity + " Water", ebx, eby);
        }
        String nutType = "Nutrient Fluid";
        if(fluidType != 0){
            nutType = "Oxygenated Nutrient Fluid";
        }
        if(this.NutrientEnergyBar.isMouseOver()){
            this.drawHoveringText(nutrientFluidAmount + " / " + nutrientCapacity + " " + nutType, ebx, eby);
        }

        fontRenderer.drawString(new TextComponentTranslation("Hydro Tank").getFormattedText(), 5, 5, Color.darkGray.getRGB());

        sync++;
        sync %= 20;
        if (sync == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetFluid(this.hydroTankTileEntity.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroTank", "waterFluidAmount", "waterCapacity", "waterFluidType"));
            this.energyBar.updateEnergyBar(waterFluidAmount, waterCapacity);

            PacketHandler.INSTANCE.sendToServer(new PacketGetFluid(this.hydroTankTileEntity.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroTank", "nutrientFluidAmount", "nutrientCapacity", "fluidType"));
            this.NutrientEnergyBar.updateEnergyBar(nutrientFluidAmount, nutrientCapacity);
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
