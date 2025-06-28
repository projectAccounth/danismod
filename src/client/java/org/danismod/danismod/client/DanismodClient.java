package org.danismod.danismod.client;

import net.fabricmc.api.ClientModInitializer;

import org.danismod.danismod.client.gui_screens.ModScreens;
import org.danismod.danismod.client.mobs_renderer.ModEntityRenderer;
import org.danismod.danismod.client.models.ModModelLayers;

public class DanismodClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityRenderer.registerRenderers();
        ModModelLayers.initialize();
        ModScreens.initialize();
    }
}
