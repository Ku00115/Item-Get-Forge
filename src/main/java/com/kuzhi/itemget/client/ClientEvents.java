package com.kuzhi.itemget.client;

import com.kuzhi.itemget.ItemGet;
import com.kuzhi.itemget.network.ItemGetNetwork;
import com.kuzhi.itemget.network.RequestRulesPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public final class ClientEvents {
    public static final KeyMapping OPEN = new KeyMapping("key.item_get.manager", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.categories.item_get");
    public static final KeyMapping CLOSE = new KeyMapping("key.item_get.close_reminder", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.item_get");

    @Mod.EventBusSubscriber(modid = ItemGet.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBus {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent event) { event.register(OPEN); event.register(CLOSE); }
    }

    @Mod.EventBusSubscriber(modid = ItemGet.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) AudioHelper.tick();
            if (event.phase == TickEvent.Phase.END) ClientHooks.tick();
            if (event.phase == TickEvent.Phase.END && OPEN.consumeClick()) ItemGetNetwork.CHANNEL.sendToServer(new RequestRulesPacket());
        }
    }
}
