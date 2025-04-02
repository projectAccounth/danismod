package org.danismod.danismod.client;

import net.fabricmc.api.ClientModInitializer;
import org.danismod.danismod.client.guiscreens.ModScreens;
import org.danismod.danismod.client.mobsrenderer.ModEntityRenderer;
import org.danismod.danismod.client.models.ModModelLayers;

public class DanismodClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityRenderer.registerRenderers();
        ModModelLayers.initialize();
        ModScreens.initialize();
    }
}
