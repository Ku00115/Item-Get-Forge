package com.kuzhi.itemget.network;

import com.kuzhi.itemget.client.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncObserverRulesPacket(String json) {
    public static void encode(SyncObserverRulesPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.json, 1048576); }
    public static SyncObserverRulesPacket decode(FriendlyByteBuf buf) { return new SyncObserverRulesPacket(buf.readUtf(1048576)); }
    public static void handle(SyncObserverRulesPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> ClientHooks.syncObserverRules(RuleJson.read(msg.json)));
        ctx.setPacketHandled(true);
    }
}
