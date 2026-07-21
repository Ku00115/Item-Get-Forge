package com.kuzhi.itemget.network;

import com.kuzhi.itemget.ItemGet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ItemGetNetwork {
    private static final String VERSION = "6";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ItemGet.MOD_ID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, RequestRulesPacket.class, RequestRulesPacket::encode, RequestRulesPacket::decode, RequestRulesPacket::handle);
        CHANNEL.registerMessage(id++, SyncRulesPacket.class, SyncRulesPacket::encode, SyncRulesPacket::decode, SyncRulesPacket::handle);
        CHANNEL.registerMessage(id++, SaveRulesPacket.class, SaveRulesPacket::encode, SaveRulesPacket::decode, SaveRulesPacket::handle);
        CHANNEL.registerMessage(id++, ShowReminderPacket.class, ShowReminderPacket::encode, ShowReminderPacket::decode, ShowReminderPacket::handle);
        CHANNEL.registerMessage(id++, RequestHistoryPacket.class, RequestHistoryPacket::encode, RequestHistoryPacket::decode, RequestHistoryPacket::handle);
        CHANNEL.registerMessage(id++, SyncHistoryPacket.class, SyncHistoryPacket::encode, SyncHistoryPacket::decode, SyncHistoryPacket::handle);
        CHANNEL.registerMessage(id++, RequestUiLayoutPacket.class, RequestUiLayoutPacket::encode, RequestUiLayoutPacket::decode, RequestUiLayoutPacket::handle);
        CHANNEL.registerMessage(id++, SaveUiLayoutPacket.class, SaveUiLayoutPacket::encode, SaveUiLayoutPacket::decode, SaveUiLayoutPacket::handle);
        CHANNEL.registerMessage(id++, SyncUiLayoutPacket.class, SyncUiLayoutPacket::encode, SyncUiLayoutPacket::decode, SyncUiLayoutPacket::handle);
        CHANNEL.registerMessage(id++, SyncObserverRulesPacket.class, SyncObserverRulesPacket::encode, SyncObserverRulesPacket::decode, SyncObserverRulesPacket::handle);
        CHANNEL.registerMessage(id, TriggerObservedPacket.class, TriggerObservedPacket::encode, TriggerObservedPacket::decode, TriggerObservedPacket::handle);
    }

    public static void requestRules() { CHANNEL.sendToServer(new RequestRulesPacket()); }
    public static void requestHistory() { CHANNEL.sendToServer(new RequestHistoryPacket()); }
    public static void requestUiLayout() { CHANNEL.sendToServer(new RequestUiLayoutPacket()); }
    public static void saveUiLayout(int x, int y) { CHANNEL.sendToServer(new SaveUiLayoutPacket(x, y)); }
    public static void triggerObserved(String ruleId) { CHANNEL.sendToServer(new TriggerObservedPacket(ruleId)); }
}
