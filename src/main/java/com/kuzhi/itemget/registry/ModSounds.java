package com.kuzhi.itemget.registry;

import com.kuzhi.itemget.ItemGet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ItemGet.MOD_ID);
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED = SOUNDS.register("item_acquired",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ItemGet.MOD_ID, "item_acquired")));
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_SOFT = register("item_acquired_soft");
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_RARE = register("item_acquired_rare");
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_MYSTIC = register("item_acquired_mystic");
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_MECHANICAL = register("item_acquired_mechanical");
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_ARCADE = register("item_acquired_arcade");
    public static final RegistryObject<SoundEvent> ITEM_ACQUIRED_RELIC = register("item_acquired_relic");
    private static RegistryObject<SoundEvent> register(String name) { return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ItemGet.MOD_ID, name))); }
    private ModSounds() {}
}
