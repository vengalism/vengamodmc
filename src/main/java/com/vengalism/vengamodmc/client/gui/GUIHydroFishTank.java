package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerHydroFishTank;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.network.PacketGetFluid;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroFishTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by vengada at 24/10/2017
 */
public class GUIHydroFishTank extends GUIHydroNutrientTank{

    public static int waterFluidAmount = 0, waterCapacity = 0;
    public static int nutrientFluidAmount = 0, nutrientCapacity = 0;
    public static int fluidType = 0, waterFluidType = 0;
    private static int sync = 0;

    //public static final ResourceLocation texturea = new ResourceLocation(Reference.MODID, "textures/gui/hydrofishgui.png");
    private TileEntityHydroFishTank hydroFishTank;
    private static int synca = 0;

    public GUIHydroFishTank(InventoryPlayer inventorySlotsIn, TileEntityHydroFishTank hydroFishTank) {
        super(new ContainerHydroFishTank(inventorySlotsIn, hydroFishTank));
        this.hydroFishTank = hydroFishTank;
        texture = new ResourceLocation(Reference.MODID, "textures/gui/hydrofishgui.png");
        xSize = 176;
        ySize = 166;
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

        fontRenderer.drawString(new TextComponentTranslation("Hydro Fish Tank").getFormattedText(), 5, 5, Color.darkGray.getRGB());

        synca++;
        synca %= 20;
        if (synca == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetFluid(this.hydroFishTank.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroFishTank", "waterFluidAmount", "waterCapacity", "waterFluidType"));
            this.energyBar.updateEnergyBar(waterFluidAmount, waterCapacity);

            PacketHandler.INSTANCE.sendToServer(new PacketGetFluid(this.hydroFishTank.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroFishTank", "nutrientFluidAmount", "nutrientCapacity", "fluidType"));
            this.NutrientEnergyBar.updateEnergyBar(nutrientFluidAmount, nutrientCapacity);
        }
    }
}
