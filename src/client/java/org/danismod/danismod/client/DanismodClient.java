package org.danismod.danismod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.danismod.danismod.client.mobsrenderer.ElephantRenderer;
import org.danismod.danismod.client.mobsrenderer.ModEntityRenderer;
import org.danismod.danismod.client.models.ElephantModel;
import org.danismod.danismod.entity.ModEntities;

public class DanismodClient implements ClientModInitializer {
    public static final EntityModelLayer ELEPHANT_MODEL_LAYER =
            new EntityModelLayer(Identifier.of("danismod", "elephant"), "main");

    @Override
    public void onInitializeClient() {
        ModEntityRenderer.registerRenderers();
        EntityRendererRegistry.register(ModEntities.ELEPHANT, ElephantRenderer::new);

        // Register Entity Model
        EntityModelLayerRegistry.registerModelLayer(ELEPHANT_MODEL_LAYER, ElephantModel::getTexturedModelData);
    }
}
