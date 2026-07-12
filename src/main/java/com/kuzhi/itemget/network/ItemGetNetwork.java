package com.kuzhi.itemget.network;

import com.kuzhi.itemget.ItemGet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ItemGetNetwork {
    private static final String VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ItemGet.MOD_ID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, RequestRulesPacket.class, RequestRulesPacket::encode, RequestRulesPacket::decode, RequestRulesPacket::handle);
        CHANNEL.registerMessage(id++, SyncRulesPacket.class, SyncRulesPacket::encode, SyncRulesPacket::decode, SyncRulesPacket::handle);
        CHANNEL.registerMessage(id++, SaveRulesPacket.class, SaveRulesPacket::encode, SaveRulesPacket::decode, SaveRulesPacket::handle);
        CHANNEL.registerMessage(id, ShowReminderPacket.class, ShowReminderPacket::encode, ShowReminderPacket::decode, ShowReminderPacket::handle);
    }
}
