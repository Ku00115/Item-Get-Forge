package com.kuzhi.itemget.network;

import com.kuzhi.itemget.client.ClientHooks;
import com.kuzhi.itemget.rule.ReminderRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record ShowReminderPacket(String json) {
    public ShowReminderPacket(ReminderRule rule) { this(RuleJson.GSON.toJson(rule)); }
    public static void encode(ShowReminderPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.json, 65535); }
    public static ShowReminderPacket decode(FriendlyByteBuf buf) { return new ShowReminderPacket(buf.readUtf(65535)); }
    public static void handle(ShowReminderPacket msg, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> ClientHooks.show(RuleJson.GSON.fromJson(msg.json, ReminderRule.class)));
        supplier.get().setPacketHandled(true);
    }
}
