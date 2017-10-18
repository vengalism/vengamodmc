/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.network;

import com.vengalism.vengamodmc.handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketGetEnergy implements IMessage {

    private boolean messageValid;

    private BlockPos pos;
    private EnumFacing side;

    private String className;
    private String energyFieldName;
    private String maxEnergyFieldName;

    public PacketGetEnergy() {

        this.messageValid = false;
    }

    public PacketGetEnergy(BlockPos pos, EnumFacing side, String className, String energyFieldName, String maxEnergyFieldName) {
        this.pos = pos;
        this.side = side;
        this.className = className;
        this.energyFieldName = energyFieldName;
        this.maxEnergyFieldName = maxEnergyFieldName;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.side = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
            this.className = ByteBufUtils.readUTF8String(buf);
            this.energyFieldName = ByteBufUtils.readUTF8String(buf);
            this.maxEnergyFieldName = ByteBufUtils.readUTF8String(buf);

        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it " + ioe.getMessage());
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (this.messageValid) {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
            ByteBufUtils.writeUTF8String(buf, this.side.getName2());
            ByteBufUtils.writeUTF8String(buf, this.className);
            ByteBufUtils.writeUTF8String(buf, this.energyFieldName);
            ByteBufUtils.writeUTF8String(buf, this.maxEnergyFieldName);
        }
    }

    public static class Handler implements IMessageHandler<PacketGetEnergy, IMessage> {

        @Override
        public IMessage onMessage(PacketGetEnergy message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER)
                return null;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        void processMessage(PacketGetEnergy message, MessageContext ctx) {
            TileEntity te = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
            if (te == null) {
                return;
            } else if (!te.hasCapability(CapabilityEnergy.ENERGY, null)) {
                return;
            } else {
                int energy = te.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
                int maxEnergy = te.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
                PacketHandler.INSTANCE.sendTo(new PacketReturnEnergy(energy, maxEnergy, message.className,
                        message.energyFieldName, message.maxEnergyFieldName), ctx.getServerHandler().player);
            }
        }
    }
}
