package com.kuzhi.itemget;

import com.kuzhi.itemget.network.ItemGetNetwork;
import com.kuzhi.itemget.registry.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ItemGet.MOD_ID)
public final class ItemGet {
    public static final String MOD_ID = "item_get";

    public ItemGet() {
        ModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemGetNetwork.register();
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }
}
