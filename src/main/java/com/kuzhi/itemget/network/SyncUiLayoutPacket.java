package com.kuzhi.itemget.network;

import com.kuzhi.itemget.client.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncUiLayoutPacket(int x, int y) {
    public static void encode(SyncUiLayoutPacket msg, FriendlyByteBuf buf) { buf.writeVarInt(msg.x); buf.writeVarInt(msg.y); }
    public static SyncUiLayoutPacket decode(FriendlyByteBuf buf) { return new SyncUiLayoutPacket(buf.readVarInt(), buf.readVarInt()); }
    public static void handle(SyncUiLayoutPacket msg, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> ClientHooks.setInventoryButtonPosition(msg.x, msg.y));
        supplier.get().setPacketHandled(true);
    }
}
