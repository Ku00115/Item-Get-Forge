package com.kuzhi.itemget.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncHistoryPacket(String json) {
    public static void encode(SyncHistoryPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.json, 1048576); }
    public static SyncHistoryPacket decode(FriendlyByteBuf buf) { return new SyncHistoryPacket(buf.readUtf(1048576)); }
    public static void handle(SyncHistoryPacket msg, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> com.kuzhi.itemget.client.ClientHooks.openHandbook(RuleJson.read(msg.json)));
        supplier.get().setPacketHandled(true);
    }
}
