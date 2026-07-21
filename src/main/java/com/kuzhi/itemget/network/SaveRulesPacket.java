package com.kuzhi.itemget.network;

import com.kuzhi.itemget.rule.RuleStore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import java.util.*;
import com.kuzhi.itemget.rule.ReminderRule;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.PacketDistributor;

public record SaveRulesPacket(String json) {
    public static void encode(SaveRulesPacket msg, FriendlyByteBuf buf) { buf.writeUtf(msg.json, 1048576); }
    public static SaveRulesPacket decode(FriendlyByteBuf buf) { return new SaveRulesPacket(buf.readUtf(1048576)); }
    public static void handle(SaveRulesPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() != null && ctx.getSender().hasPermissions(2)) {
                RuleStore store = RuleStore.get(ctx.getSender().serverLevel());
                List<ReminderRule> updated = RuleJson.read(msg.json);
                Map<String, ReminderRule> old = new HashMap<>(); store.rules().forEach(r -> old.put(r.id, r));
                Set<String> changed = new HashSet<>();
                for (ReminderRule rule : updated) {
                    ReminderRule before = old.get(rule.id);
                    boolean triggerChanged = before == null || !Objects.equals(before.triggerType, rule.triggerType) || !Objects.equals(before.trigger, rule.trigger);
                    if (triggerChanged) changed.add(rule.id);
                    rule.triggerRevision = before == null ? Math.max(1, rule.triggerRevision) : triggerChanged ? Math.max(1, before.triggerRevision) + 1 : Math.max(1, before.triggerRevision);
                }
                store.replace(updated);
                for (var player : ctx.getSender().server.getPlayerList().getPlayers())
                    ItemGetNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncObserverRulesPacket(RuleJson.write(store.rules())));
                if (!changed.isEmpty()) for (var player : ctx.getSender().server.getPlayerList().getPlayers()) {
                    CompoundTag data = player.getPersistentData().getCompound("item_get_data"); CompoundTag shown = data.getCompound("shown"), totals = data.getCompound("totals"), states=data.getCompound("condition_states");
                    changed.forEach(id -> { shown.remove(id); totals.remove(id); states.remove(id); }); data.put("shown", shown); data.put("totals", totals);data.put("condition_states",states); player.getPersistentData().put("item_get_data", data);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
