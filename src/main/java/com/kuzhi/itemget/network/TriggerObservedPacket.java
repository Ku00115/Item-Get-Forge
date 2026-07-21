package com.kuzhi.itemget.network;

import com.kuzhi.itemget.ServerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TriggerObservedPacket(String ruleId) {
    public static void encode(TriggerObservedPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.ruleId, 128); }
    public static TriggerObservedPacket decode(FriendlyByteBuf buf) { return new TriggerObservedPacket(buf.readUtf(128)); }
    public static void handle(TriggerObservedPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> { if (ctx.getSender() != null) ServerEvents.triggerObserved(ctx.getSender(), msg.ruleId); });
        ctx.setPacketHandled(true);
    }
}
