package com.vengalism.vengamodmc.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;

public class PacketReturnFluid implements IMessage {

    private boolean messageValid;

    private int fluidAmount;
    private int capacity;
    private int fluidType;

    private String className;

    private String fluidAmountFieldName;
    private String capacityFieldName;
    private String fluidTypeName;

    public PacketReturnFluid() {
        this.messageValid = false;
    }

    PacketReturnFluid(int fluidAmount, int capacity, String className, String fluidAmountFieldName, String capacityFieldName, int fluidType, String fluidTypeName) {

        this.fluidAmount = fluidAmount;
        this.capacity = capacity;
        this.className = className;
        this.fluidAmountFieldName = fluidAmountFieldName;
        this.capacityFieldName = capacityFieldName;
        this.fluidType = fluidType;
        this.fluidTypeName = fluidTypeName;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        try {
            this.fluidAmount = buf.readInt();
            this.capacity = buf.readInt();
            this.className = ByteBufUtils.readUTF8String(buf);
            this.fluidAmountFieldName = ByteBufUtils.readUTF8String(buf);
            this.capacityFieldName = ByteBufUtils.readUTF8String(buf);
            this.fluidType = buf.readInt();
            this.fluidTypeName = ByteBufUtils.readUTF8String(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it " + ioe.getMessage());
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (this.messageValid) {
            buf.writeInt(this.fluidAmount);
            buf.writeInt(this.capacity);
            ByteBufUtils.writeUTF8String(buf, this.className);
            ByteBufUtils.writeUTF8String(buf, this.fluidAmountFieldName);
            ByteBufUtils.writeUTF8String(buf, this.capacityFieldName);
            buf.writeInt(this.fluidType);
            ByteBufUtils.writeUTF8String(buf, this.fluidTypeName);
        }
    }

    public static class Handler implements IMessageHandler<PacketReturnFluid, IMessage> {
        @Override
        public IMessage onMessage(PacketReturnFluid message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT)
                return null;
            Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
            return null;
        }

        void processMessage(PacketReturnFluid message) {
            try {
                Class clazz = Class.forName(message.className);
                Field energyField = clazz.getDeclaredField(message.fluidAmountFieldName);
                Field capacityField = clazz.getDeclaredField(message.capacityFieldName);
                Field fluidTypeFiled = clazz.getDeclaredField(message.fluidTypeName);
                energyField.setInt(clazz, message.fluidAmount);
                capacityField.setInt(clazz, message.capacity);
                fluidTypeFiled.setInt(clazz, message.fluidType);
            } catch (Exception e) {
                System.out.println("You Stuffed it " + e.getMessage());
                System.out.println(e.getCause());
                System.out.println(e.getStackTrace());

            }

        }

    }
}
