/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.handlers;

import com.vengalism.vengamodmc.Reference;
import com.vengalism.vengamodmc.network.PacketGetEnergy;
import com.vengalism.vengamodmc.network.PacketReturnEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {


    public static SimpleNetworkWrapper INSTANCE;

    public static void init(){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
        INSTANCE.registerMessage(PacketReturnEnergy.Handler.class, PacketReturnEnergy.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(PacketGetEnergy.Handler.class, PacketGetEnergy.class, 1, Side.SERVER);
        //INSTANCE.registerMessage(PacketReturnFluid.Handler.class, PacketReturnFluid.class, 2, Side.CLIENT);
        //INSTANCE.registerMessage(PacketGetFluid.Handler.class, PacketGetFluid.class, 3, Side.SERVER);
    }
}
