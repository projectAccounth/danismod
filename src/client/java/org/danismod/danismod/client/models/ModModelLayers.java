package org.danismod.danismod.client.models;

import org.danismod.danismod.Danismod;
import org.danismod.danismod.client.models.entities.BuffaloModel;
import org.danismod.danismod.client.models.entities.ElephantModel;
import org.danismod.danismod.client.models.entities.LionModel;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer ELEPHANT_MODEL_LAYER =
            new EntityModelLayer(Identifier.of(Danismod.MOD_ID, "elephant"), "main");
    public static final EntityModelLayer NORMAL_LION_MODEL_LAYER =
            new EntityModelLayer(Identifier.of(Danismod.MOD_ID, "lion"), "standing");
    public static final EntityModelLayer RESTING_LION_MODEL_LAYER =
            new EntityModelLayer(Identifier.of(Danismod.MOD_ID, "lion"), "laying");
    public static final EntityModelLayer BUFFALO_MODEL_LAYER =
            new EntityModelLayer(Identifier.of(Danismod.MOD_ID, "buffalo"), "main");

    public static void initialize() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.ELEPHANT_MODEL_LAYER, ElephantModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NORMAL_LION_MODEL_LAYER, LionModel::getStandingModel);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.RESTING_LION_MODEL_LAYER, LionModel::getLyingModel);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BUFFALO_MODEL_LAYER, BuffaloModel::getTexturedModelData);
    }
}
