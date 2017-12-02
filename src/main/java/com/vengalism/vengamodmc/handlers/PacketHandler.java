/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.handlers;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.network.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
//TODO is it possible to use JSON objects to send over network and then have the gui fetch data from JSON object, would
    //allow for 1 type of packetGetter and Return right? maybe, maybe not

    public static SimpleNetworkWrapper INSTANCE;

    public static void init(){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
        INSTANCE.registerMessage(PacketReturnEnergy.Handler.class, PacketReturnEnergy.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(PacketGetEnergy.Handler.class, PacketGetEnergy.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketReturnFluid.Handler.class, PacketReturnFluid.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(PacketGetFluid.Handler.class, PacketGetFluid.class, 3, Side.SERVER);
        INSTANCE.registerMessage(PacketReturnData.Handler.class, PacketReturnData.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(PacketGetData.Handler.class, PacketGetData.class, 5, Side.SERVER);

    }
}
