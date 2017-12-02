package com.vengalism.vengamodmc.client.gui;

import com.google.gson.JsonObject;
import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.container.ContainerHydroFishTank;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.network.PacketGetData;
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
    public static JsonObject data = new JsonObject();
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
        data.addProperty("valid", false);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        displayPacketInfo();

        synca++;
        synca %= 20;
        if (synca == 0) {

            PacketHandler.INSTANCE.sendToServer(new PacketGetData(this.hydroFishTank.getPos(),
                    EnumFacing.NORTH, "com.vengalism.vengamodmc.client.gui.GUIHydroFishTank", "data"));
        }
    }
}
