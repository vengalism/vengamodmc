package com.vengalism.vengamodmc.network;


import com.google.gson.JsonObject;
import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityBase;
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

/**
 * Created by vengada at 2/12/2017
 */
public class PacketGetData implements IMessage{

    private boolean messageValid;

    private JsonObject data;
    private BlockPos pos;
    private EnumFacing side;
    private String className;
    private String dataFieldName;

    public PacketGetData(){
        this.messageValid = false;
    }

    public PacketGetData(BlockPos pos, EnumFacing side, String className, String dataFieldName){
        this.pos = pos;
        this.side = side;
        this.className = className;
        this.dataFieldName = dataFieldName;
        this.messageValid = true;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.side = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
            this.className = ByteBufUtils.readUTF8String(buf);
            this.dataFieldName = ByteBufUtils.readUTF8String(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it JOw" + ioe.getMessage());
            return;
        }
        this.messageValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if(this.messageValid){
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
            ByteBufUtils.writeUTF8String(buf, this.side.getName2());
            ByteBufUtils.writeUTF8String(buf, this.className);
            ByteBufUtils.writeUTF8String(buf, this.dataFieldName);
        }
    }

    public static class Handler implements IMessageHandler<PacketGetData, IMessage> {

        @Override
        public IMessage onMessage(PacketGetData message, MessageContext ctx) {
            if (!message.messageValid && ctx.side != Side.SERVER)
                return null;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        void processMessage(PacketGetData message, MessageContext ctx) {
            TileEntity te = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
            if (te == null) {
                return;
            } else if (!(te instanceof TileEntityBase)) {
                return;
            } else {
                JsonObject data = ((TileEntityBase) te).getPacketData();
                PacketHandler.INSTANCE.sendTo(new PacketReturnData(data, message.className,
                        message.dataFieldName), ctx.getServerHandler().player);
            }
        }
    }
}
