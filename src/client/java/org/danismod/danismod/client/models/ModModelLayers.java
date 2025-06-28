package org.danismod.danismod.client.models;

import org.danismod.danismod.Danismod;
import org.danismod.danismod.client.models.entities.BuffaloModel;
import org.danismod.danismod.client.models.entities.ElephantModel;
import org.danismod.danismod.client.models.entities.LionModel;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModModelLayers {
    private static EntityModelLayer layer(String id, String name) {
        return new EntityModelLayer(Identifier.of(Danismod.MOD_ID, id), name);
    }

    public static final EntityModelLayer ELEPHANT_MODEL_LAYER = layer("elephant", "main");
    public static final EntityModelLayer NORMAL_LION_MODEL_LAYER = layer("lion", "standing");
    public static final EntityModelLayer RESTING_LION_MODEL_LAYER = layer("lion", "laying");
    public static final EntityModelLayer BUFFALO_MODEL_LAYER = layer("buffalo", "main");

    public static void initialize() {
        EntityModelLayerRegistry.registerModelLayer(ELEPHANT_MODEL_LAYER, ElephantModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(NORMAL_LION_MODEL_LAYER, LionModel::getStandingModel);
        EntityModelLayerRegistry.registerModelLayer(RESTING_LION_MODEL_LAYER, LionModel::getLyingModel);
        EntityModelLayerRegistry.registerModelLayer(BUFFALO_MODEL_LAYER, BuffaloModel::getTexturedModelData);
    }
}