package org.danismod.danismod.client;

import net.fabricmc.api.ClientModInitializer;
import org.danismod.danismod.client.guiscreens.ModScreens;
import org.danismod.danismod.client.mob_renderers.ModEntityRenderer;
import org.danismod.danismod.client.models.ModModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DanismodClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityRenderer.registerRenderers();
        ModModelLayers.initialize();
        ModScreens.initialize();
    }
}
