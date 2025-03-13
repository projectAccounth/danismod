package org.danismod.danismod;

import org.danismod.danismod.entity.ModEntities;
import org.danismod.danismod.entity.ModEntitySpawns;
import org.danismod.danismod.items.ModItems;
import net.fabricmc.api.ModInitializer;
import org.danismod.danismod.sounds.ModSoundEvents;

public class Danismod implements ModInitializer {
    public static final String MOD_ID = "danismod";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModEntities.register();
        ModEntitySpawns.registerSpawns();
        ModSoundEvents.registerModSounds();
    }
}
