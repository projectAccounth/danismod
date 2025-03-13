package org.danismod.danismod.client.mobsrenderer;

import org.danismod.danismod.entity.ModEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ModEntityRenderer {
    public static void registerRenderers() {
        EntityRendererRegistry.register(ModEntities.ELEPHANT, ElephantRenderer::new);
        EntityRendererRegistry.register(ModEntities.LION, LionRenderer::new);
    }
}