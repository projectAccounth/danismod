package org.danismod.danismod.client.guiscreens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.danismod.danismod.screenhandlers.ModScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModScreens {

    public static void initialize() {
        HandledScreens.register(ModScreenHandlers.GRINDER_SCREEN_HANDLER, GrinderScreen::new);
    }
}
