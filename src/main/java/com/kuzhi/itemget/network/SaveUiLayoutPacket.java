package com.kuzhi.itemget.network;

import com.kuzhi.itemget.rule.UiLayoutStore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record SaveUiLayoutPacket(int x, int y) {
    public static void encode(SaveUiLayoutPacket msg, FriendlyByteBuf buf) { buf.writeVarInt(msg.x); buf.writeVarInt(msg.y); }
    public static SaveUiLayoutPacket decode(FriendlyByteBuf buf) { return new SaveUiLayoutPacket(buf.readVarInt(), buf.readVarInt()); }
    public static void handle(SaveUiLayoutPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() == null) return;
            int x = Math.max(-1, Math.min(4096, msg.x));
            int y = Math.max(-1, Math.min(4096, msg.y));
            UiLayoutStore.get(ctx.getSender().serverLevel()).setInventoryButton(x, y);
            ItemGetNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(ctx::getSender), new SyncUiLayoutPacket(x, y));
        });
        ctx.setPacketHandled(true);
    }
}
