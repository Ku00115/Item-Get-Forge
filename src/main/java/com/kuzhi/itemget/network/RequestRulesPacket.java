package com.kuzhi.itemget.network;

import com.kuzhi.itemget.rule.RuleStore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import java.util.stream.Collectors;

public record RequestRulesPacket() {
    public static void encode(RequestRulesPacket msg, FriendlyByteBuf buf) {}
    public static RequestRulesPacket decode(FriendlyByteBuf buf) { return new RequestRulesPacket(); }
    public static void handle(RequestRulesPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() != null) ItemGetNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(ctx::getSender),
                    new SyncRulesPacket(RuleJson.write(RuleStore.get(ctx.getSender().serverLevel()).rules()), ctx.getSender().hasPermissions(2),
                            ctx.getSender().serverLevel().registryAccess().registryOrThrow(Registries.BIOME).keySet().stream().map(Object::toString).sorted().collect(Collectors.joining("\n")),
                            ctx.getSender().serverLevel().registryAccess().registryOrThrow(Registries.STRUCTURE).keySet().stream().map(Object::toString).sorted().collect(Collectors.joining("\n"))));
        });
        ctx.setPacketHandled(true);
    }
}
