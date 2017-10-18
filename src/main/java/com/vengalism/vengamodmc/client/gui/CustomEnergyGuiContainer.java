/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.client.gui;

import com.vengalism.vengamodmc.energy.EnergyBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class CustomEnergyGuiContainer extends GuiContainer {

    EnergyBar energyBar;
    int ebx, eby;

    CustomEnergyGuiContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.ebx = this.width /2 - this.xSize /2;
        this.eby = this.height /2 - this.ySize/2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    EnergyBar getDefaultEnergyBar(){
        ebx = this.width /2 - this.xSize /2;
        eby = this.height /2 - this.ySize/2;
        return new EnergyBar(0,  ebx + 152, eby +11, 18, 60, 0, 0 );
    }

    EnergyBar getNextToDefaultEnergyBar(){
        ebx = this.width /2 - this.xSize /2;
        eby = this.height /2 - this.ySize/2;
        return new EnergyBar(1,  ebx + 152-20, eby +11, 18, 60, 0, 0 );
    }
}
