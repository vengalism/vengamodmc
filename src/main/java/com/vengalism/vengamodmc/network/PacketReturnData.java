package com.vengalism.vengamodmc.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;

/**
 * Created by vengada at 2/12/2017
 */
public class PacketReturnData implements IMessage{

    private boolean messageValid;

    private JsonObject data;

    private String className;
    private String dataFieldName;

    public PacketReturnData(){
        this.messageValid = false;
    }

    public PacketReturnData(JsonObject data, String className, String dataFieldName){
        this.data = data;
        this.className = className;
        this.dataFieldName = dataFieldName;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {//magic!!
            this.data = new JsonParser().parse(ByteBufUtils.readUTF8String(buf)).getAsJsonObject();
            this.className = ByteBufUtils.readUTF8String(buf);
            this.dataFieldName = ByteBufUtils.readUTF8String(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it " + ioe.getMessage());
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if(this.messageValid){
            ByteBufUtils.writeUTF8String(buf, this.data.toString());
            ByteBufUtils.writeUTF8String(buf, this.className);
            ByteBufUtils.writeUTF8String(buf, this.dataFieldName);
        }
    }

    public static class Handler implements IMessageHandler<PacketReturnData, IMessage>{

        @Override
        public IMessage onMessage(PacketReturnData message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.CLIENT)
                return null;
            Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
            return null;
        }

        void processMessage(PacketReturnData message) {
            try {
                Class clazz = Class.forName(message.className);
                Field dataField = clazz.getDeclaredField(message.dataFieldName);
                dataField.set(clazz, message.data);
            } catch (Exception e) {
                System.out.println("You Stuffed it " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
