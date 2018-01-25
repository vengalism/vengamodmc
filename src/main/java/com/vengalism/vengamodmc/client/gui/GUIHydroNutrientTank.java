package com.vengalism.vengamodmc.client.gui;

import com.google.gson.JsonObject;
import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerHydroTank;
import com.vengalism.vengamodmc.energy.EnergyBar;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.network.PacketGetData;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroNutrientTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;


public class GUIHydroNutrientTank extends CustomEnergyGuiContainer {

    public static ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/hydro_tank_gui.png");
    private TileEntityHydroNutrientTank hydroTankTileEntity;
    public static int waterFluidAmount = 0, waterCapacity = 0;
    public static int nutrientFluidAmount = 0, nutrientCapacity = 0;
    public static int fluidType = 0, waterFluidType = 0;
    public static JsonObject data = new JsonObject();
    private static int sync = 0;
    public EnergyBar NutrientEnergyBar;


    public GUIHydroNutrientTank(Container container){
        super(container);

    }

    public GUIHydroNutrientTank(InventoryPlayer player, TileEntityHydroNutrientTank hydroTankTileEntity) {
        super(new ContainerHydroTank(player, hydroTankTileEntity));
        this.hydroTankTileEntity = hydroTankTileEntity;
        xSize = 176;
        ySize = 166;
        data.addProperty("valid", false);
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
    public void displayPacketInfo() {
        if(data.has("valid")){
            if(data.get("valid").getAsBoolean()){
                JsonObject fluidStorage = data.getAsJsonObject("fluidStorage");

                JsonObject extraTank = data.getAsJsonObject("extraTank");
                if(extraTank != null){
                    if(this.NutrientEnergyBar.isMouseOver()){
                        this.drawHoveringText(extraTank.get("fluidAmount") + " / " + extraTank.get("fluidMaxAmount") + " " + extraTank.get("fluidName") , ebx, eby);
                        this.NutrientEnergyBar.updateEnergyBar(extraTank.get("fluidAmount").getAsInt(), extraTank.get("fluidMaxAmount").getAsInt());
                    }
                }

                if(fluidStorage != null){
                    if(this.energyBar.isMouseOver()){
                        this.drawHoveringText(fluidStorage.get("fluidAmount") + " / " + fluidStorage.get("fluidMaxAmount") + " " + fluidStorage.get("fluidName") , ebx, eby);
                        this.energyBar.updateEnergyBar(fluidStorage.get("fluidAmount").getAsInt(), fluidStorage.get("fluidMaxAmount").getAsInt());
                    }
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
            PacketHandler.INSTANCE.sendToServer(new PacketGetData(this.hydroTankTileEntity.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroNutrientTank", "data"));
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
