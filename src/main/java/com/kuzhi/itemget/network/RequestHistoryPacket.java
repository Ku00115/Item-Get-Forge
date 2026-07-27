package com.kuzhi.itemget.network;

import com.kuzhi.itemget.rule.ReminderRule;
import com.kuzhi.itemget.rule.RuleStore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public record RequestHistoryPacket() {
    public static void encode(RequestHistoryPacket msg, FriendlyByteBuf buf) {}
    public static RequestHistoryPacket decode(FriendlyByteBuf buf) { return new RequestHistoryPacket(); }
    public static void handle(RequestHistoryPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() != null) ItemGetNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(ctx::getSender), new SyncHistoryPacket(RuleJson.write(history(ctx.getSender()))));
        });
        ctx.setPacketHandled(true);
    }

    private static List<ReminderRule> history(net.minecraft.server.level.ServerPlayer player) {
        List<ReminderRule> out = new ArrayList<>();
        Map<String, ReminderRule> savedByKey = new LinkedHashMap<>();
        Map<String, Integer> savedOrder = new LinkedHashMap<>();
        CompoundTag data = player.getPersistentData().getCompound("item_get_data");
        ListTag saved = data.getList("history", Tag.TAG_STRING);
        for (Tag tag : saved) try {
            ReminderRule rule = RuleJson.GSON.fromJson(tag.getAsString(), ReminderRule.class);
            if (rule != null) {
                savedOrder.putIfAbsent(key(rule), savedOrder.size());
                savedByKey.put(key(rule), rule);
            }
        } catch (RuntimeException ignored) {}
        CompoundTag shown = data.getCompound("shown");
        Set<String> currentKeys = new HashSet<>();
        for (ReminderRule rule : RuleStore.get(player.serverLevel()).rules()) {
            String key = key(rule);
            currentKeys.add(key);
            boolean unlocked = shown.contains(rule.id) && shown.getInt(rule.id) == Math.max(1, rule.triggerRevision) || savedByKey.containsKey(key);
            out.add(handbookCopy(rule, unlocked, savedOrder.getOrDefault(key, -1)));
        }
        for (Map.Entry<String, ReminderRule> entry : savedByKey.entrySet()) {
            if (!currentKeys.contains(entry.getKey())) out.add(handbookCopy(entry.getValue(), true, savedOrder.getOrDefault(entry.getKey(), -1)));
        }
        return out;
    }

    private static ReminderRule handbookCopy(ReminderRule rule, boolean unlocked, int order) {
        ReminderRule copy = RuleJson.GSON.fromJson(RuleJson.GSON.toJson(rule), ReminderRule.class);
        if (copy.trigger == null) copy.trigger = new com.google.gson.JsonObject();
        copy.trigger.addProperty("_handbook_unlocked", unlocked);
        copy.trigger.addProperty("_handbook_order", order);
        return copy;
    }

    private static String key(ReminderRule rule) {
        return String.valueOf(rule.id) + "@" + Math.max(1, rule.triggerRevision);
    }
}
