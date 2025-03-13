package org.danismod.danismod.client.models;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer ELEPHANT_MODEL_LAYER =
            new EntityModelLayer(Identifier.of("danismod", "elephant"), "main");
    public static final EntityModelLayer NORMAL_LION_MODEL_LAYER =
            new EntityModelLayer(Identifier.of("danismod", "lion"), "standing");
    public static final EntityModelLayer RESTING_LION_MODEL_LAYER =
            new EntityModelLayer(Identifier.of("danismod", "lion"), "laying");
    public static final EntityModelLayer NORMAL_LION_MODEL_LAYER_F =
            new EntityModelLayer(Identifier.of("danismod", "lion"), "standing_f");
    public static final EntityModelLayer RESTING_LION_MODEL_LAYER_F =
            new EntityModelLayer(Identifier.of("danismod", "lion"), "laying_f");

    public static void initialize() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.ELEPHANT_MODEL_LAYER, ElephantModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NORMAL_LION_MODEL_LAYER, LionModel::getStandingModel);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.RESTING_LION_MODEL_LAYER, LionModel::getLyingModel);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NORMAL_LION_MODEL_LAYER_F, LionModel::getFemaleStandingModel);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.RESTING_LION_MODEL_LAYER_F, LionModel::getFemaleLyingModel);
    }
}
