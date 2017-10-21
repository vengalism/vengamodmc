package com.vengalism.vengamodmc.network;

import com.vengalism.vengamodmc.handlers.PacketHandler;
import com.vengalism.vengamodmc.tileentities.TileEntityHydroTank;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketGetFluid implements IMessage {

    private boolean messageValid;

    private BlockPos pos;
    private EnumFacing side;

    private String className;
    private String fluidAmountFieldName;
    private String capacityFieldName;
    private String fluidTypeName;

    public PacketGetFluid() {
        this.messageValid = false;
    }

    public PacketGetFluid(BlockPos pos, EnumFacing side, String className, String fluidAmountFieldName, String capacityFieldName, String fluidTypeName){
        this.pos = pos;
        this.side = side;
        this.className = className;
        this.fluidAmountFieldName = fluidAmountFieldName;
        this.capacityFieldName = capacityFieldName;
        this.fluidTypeName = fluidTypeName;
        this.messageValid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.side = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
            this.className = ByteBufUtils.readUTF8String(buf);
            this.fluidAmountFieldName = ByteBufUtils.readUTF8String(buf);
            this.capacityFieldName = ByteBufUtils.readUTF8String(buf);
            this.fluidTypeName = ByteBufUtils.readUTF8String(buf);

        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("You Stuffed it " + ioe.getMessage());
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
            ByteBufUtils.writeUTF8String(buf, this.fluidAmountFieldName);
            ByteBufUtils.writeUTF8String(buf, this.capacityFieldName);
            ByteBufUtils.writeUTF8String(buf, this.fluidTypeName);
        }
    }

    public static class Handler implements IMessageHandler<PacketGetFluid, IMessage> {

        @Override
        public IMessage onMessage(PacketGetFluid message, MessageContext ctx) {

            if (!message.messageValid && ctx.side != Side.SERVER)
                return null;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
            return null;
        }

        void processMessage(PacketGetFluid message, MessageContext ctx) {

            TileEntity te = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
            if (te == null) {
                return;
            } else if (!te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                return;
            } else if (te instanceof TileEntityHydroTank && message.fluidAmountFieldName.equals("nutrientFluidAmount")) {

                TileEntityHydroTank hydroTankTileEntity = (TileEntityHydroTank) te;
                IFluidTank fluidTank = hydroTankTileEntity.getNutrientTank();
                if(fluidTank != null) {
                    int fluidAmount = fluidTank.getFluidAmount();
                    int capacity = fluidTank.getCapacity();
                    int fluidType = 0;
                    String nutType;
                    if(fluidTank.getFluid() != null) {
                        if(fluidTank.getFluid().getUnlocalizedName() != null){
                            nutType = fluidTank.getFluid().getUnlocalizedName();
                            if (nutType != null) {
                                if (nutType.contains("oxygenated"))
                                    fluidType = 1;
                            }
                        }

                    }
                    PacketHandler.INSTANCE.sendTo(new PacketReturnFluid(fluidAmount, capacity, message.className,
                            message.fluidAmountFieldName, message.capacityFieldName, fluidType, message.fluidTypeName), ctx.getServerHandler().player);
                }
            } else {
                IFluidTank fluidTank = (IFluidTank)te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                if(fluidTank != null) {
                    int fluidAmount = fluidTank.getFluidAmount();
                    int capacity = fluidTank.getCapacity();
                    PacketHandler.INSTANCE.sendTo(new PacketReturnFluid(fluidAmount, capacity, message.className,
                            message.fluidAmountFieldName, message.capacityFieldName, 0, message.fluidTypeName), ctx.getServerHandler().player);
                }
            }
        }
    }
}
