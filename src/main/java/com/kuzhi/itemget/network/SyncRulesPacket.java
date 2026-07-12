package com.kuzhi.itemget.network;

import com.kuzhi.itemget.client.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record SyncRulesPacket(String json, boolean editable, String biomes, String structures) {
    public static void encode(SyncRulesPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.json, 1048576); buf.writeBoolean(msg.editable); buf.writeUtf(msg.biomes,1048576); buf.writeUtf(msg.structures,1048576); }
    public static SyncRulesPacket decode(FriendlyByteBuf buf) { return new SyncRulesPacket(buf.readUtf(1048576), buf.readBoolean(),buf.readUtf(1048576),buf.readUtf(1048576)); }
    public static void handle(SyncRulesPacket msg, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> ClientHooks.openManager(RuleJson.read(msg.json), msg.editable, msg.biomes, msg.structures));
        supplier.get().setPacketHandled(true);
    }
}
