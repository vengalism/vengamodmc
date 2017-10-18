/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;

public class PacketReturnEnergy implements IMessage {

    private boolean messageValid;

    private int energy;
    private int maxEnergy;

    private String className;
    private String energyFieldName;
    private String maxEnergyFieldName;

    public PacketReturnEnergy() {
        this.messageValid = false;
    }

    PacketReturnEnergy(int energy, int maxEnergy, String className, String energyFieldName, String maxEnergyFieldName) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.className = className;
        this.energyFieldName = energyFieldName;
        this.maxEnergyFieldName = maxEnergyFieldName;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        try {
            this.energy = buf.readInt();
            this.maxEnergy = buf.readInt();
            this.className = ByteBufUtils.readUTF8String(buf);
            this.energyFieldName = ByteBufUtils.readUTF8String(buf);
            this.maxEnergyFieldName = ByteBufUtils.readUTF8String(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it " + ioe.getMessage());
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (this.messageValid) {
            buf.writeInt(this.energy);
            buf.writeInt(this.maxEnergy);
            ByteBufUtils.writeUTF8String(buf, this.className);
            ByteBufUtils.writeUTF8String(buf, this.energyFieldName);
            ByteBufUtils.writeUTF8String(buf, this.maxEnergyFieldName);
        }
    }

    public static class Handler implements IMessageHandler<PacketReturnEnergy, IMessage> {
        @Override
        public IMessage onMessage(PacketReturnEnergy message, MessageContext ctx) {

            if (!message.messageValid && ctx.side != Side.CLIENT)
                return null;
            Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
            return null;
        }

        void processMessage(PacketReturnEnergy message) {
            try {
                Class clazz = Class.forName(message.className);
                Field energyField = clazz.getDeclaredField(message.energyFieldName);
                Field capacityField = clazz.getDeclaredField(message.maxEnergyFieldName);
                energyField.setInt(clazz, message.energy);
                capacityField.setInt(clazz, message.maxEnergy);
            } catch (Exception e) {
                System.out.println("You Stuffed it " + e.getMessage());
            }
        }
    }
}
