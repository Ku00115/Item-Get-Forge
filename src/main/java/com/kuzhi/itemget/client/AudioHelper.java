package com.kuzhi.itemget.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class AudioHelper {
    private AudioHelper() {}

    public static boolean exists(String id) {
        ResourceLocation key = ResourceLocation.tryParse(id == null ? "" : id.trim());
        return key != null && (ForgeRegistries.SOUND_EVENTS.containsKey(key) || CustomSoundLibrary.isCustom(id));
    }

    public static boolean play(String id) {
        if (CustomSoundLibrary.isCustom(id)) return ExternalAudioPlayer.play(CustomSoundLibrary.resolve(id));
        ResourceLocation key = ResourceLocation.tryParse(id == null ? "" : id.trim());
        SoundEvent sound = key == null ? null : ForgeRegistries.SOUND_EVENTS.getValue(key);
        if (sound == null) return false;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F));
        return true;
    }

    public static void tick() { ExternalAudioPlayer.tick(); }
}
