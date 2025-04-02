package org.danismod.danismod.client.guiscreens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.danismod.danismod.screenhandlers.ModScreenHandlers;

public class ModScreens {

    public static void initialize() {
        HandledScreens.register(ModScreenHandlers.GRINDER_SCREEN_HANDLER, GrinderScreen::new);
    }
}
