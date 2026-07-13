package com.kuzhi.itemget.network;

import com.kuzhi.itemget.rule.UiLayoutStore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record RequestUiLayoutPacket() {
    public static void encode(RequestUiLayoutPacket msg, FriendlyByteBuf buf) {}
    public static RequestUiLayoutPacket decode(FriendlyByteBuf buf) { return new RequestUiLayoutPacket(); }
    public static void handle(RequestUiLayoutPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() == null) return;
            UiLayoutStore store = UiLayoutStore.get(ctx.getSender().serverLevel());
            ItemGetNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(ctx::getSender), new SyncUiLayoutPacket(store.inventoryButtonX(), store.inventoryButtonY()));
        });
        ctx.setPacketHandled(true);
    }
}
