package org.danismod.danismod.sounds;

import org.danismod.danismod.Danismod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent ENTITY_LION_ROAR = registerSound("entity.lion.roar");
    public static final SoundEvent ENTITY_LION_GROWL = registerSound("entity.lion.growl");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(Danismod.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerModSounds() {
    }
}
