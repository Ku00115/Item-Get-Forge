package com.kuzhi.itemget.client;

import com.kuzhi.itemget.ItemGet;
import com.kuzhi.itemget.network.ItemGetNetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public final class ClientEvents {
    public static final KeyMapping OPEN = new KeyMapping("key.item_get.manager", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.item_get");
    public static final KeyMapping HANDBOOK = new KeyMapping("key.item_get.handbook", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.item_get");
    public static final KeyMapping PONDER = new KeyMapping("key.item_get.open_ponder", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.categories.item_get");
    public static final KeyMapping CREATE_PONDER = new KeyMapping("key.item_get.open_create_ponder", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.item_get");
    public static final KeyMapping CLOSE = new KeyMapping("key.item_get.close_reminder", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.item_get");

    private static void openHandbook() {
        ItemGetNetwork.requestHistory();
        ClientHooks.openCachedHandbook();
    }

    @Mod.EventBusSubscriber(modid = ItemGet.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBus {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent event) { event.register(OPEN); event.register(HANDBOOK); event.register(PONDER); event.register(CREATE_PONDER); event.register(CLOSE); }
    }

    @Mod.EventBusSubscriber(modid = ItemGet.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) AudioHelper.tick();
            if (event.phase == TickEvent.Phase.END) ClientHooks.tick();
            if (event.phase == TickEvent.Phase.END) ObservationTracker.tick();
            if (event.phase == TickEvent.Phase.END && OPEN.consumeClick()) ItemGetNetwork.requestRules();
            if (event.phase == TickEvent.Phase.END && HANDBOOK.consumeClick() && !SideReminderOverlay.openLatestDetail()) openHandbook();
            if (event.phase == TickEvent.Phase.END && PONDER.consumeClick()) SideReminderOverlay.openLatestPonder();
            if (event.phase == TickEvent.Phase.END && CREATE_PONDER.consumeClick()) SideReminderOverlay.openLatestCreatePonder();
        }

        @SubscribeEvent public static void renderGui(RenderGuiEvent.Post event) {
            SideReminderOverlay.render(event.getGuiGraphics(), event.getPartialTick());
        }

        @SubscribeEvent public static void renderScreen(ScreenEvent.Render.Post event) {
            ObservationTracker.captureHoveredItem(event.getScreen());
            InventoryHandbookButton.renderCurrent(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
            SideReminderOverlay.render(event.getGuiGraphics(), event.getPartialTick());
        }

        @SubscribeEvent public static void commands(RegisterClientCommandsEvent event) {
            event.getDispatcher().register(Commands.literal("itemget")
                    .then(Commands.literal("handbook").executes(ctx -> { openHandbook(); return 1; }))
                    .then(Commands.literal("manager").executes(ctx -> { ItemGetNetwork.requestRules(); return 1; })));
        }

        @SubscribeEvent public static void screenInit(ScreenEvent.Init.Post event) {
            if (event.getScreen() instanceof InventoryScreen && Minecraft.getInstance().player != null) {
                event.addListener(new InventoryHandbookButton(event.getScreen().width, event.getScreen().height));
            }
        }

        @SubscribeEvent public static void mousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
            if (InventoryHandbookButton.press(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton())) event.setCanceled(true);
        }

        @SubscribeEvent public static void mouseDragged(ScreenEvent.MouseDragged.Pre event) {
            if (InventoryHandbookButton.dragCurrent(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton())) event.setCanceled(true);
        }

        @SubscribeEvent public static void mouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
            if (InventoryHandbookButton.release(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton())) event.setCanceled(true);
        }
    }
}
