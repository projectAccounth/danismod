package org.danismod.danismod.client.mobsrenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobEntityRenderer;
import org.danismod.danismod.client.DanismodClient;
import org.danismod.danismod.client.mobsrenderer.renderstates.ElephantRenderState;
import org.danismod.danismod.entity.Elephant;
import org.danismod.danismod.client.models.ElephantModel;

import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRendererFactory;

@Environment(EnvType.CLIENT)
public class ElephantRenderer extends MobEntityRenderer<Elephant, ElephantRenderState, ElephantModel> {
    private static final Identifier TEXTURE = Identifier.of("danismod", "textures/entities/elephant.png");

    public ElephantRenderer(EntityRendererFactory.Context context) {
        super(context, new ElephantModel(context.getPart(DanismodClient.ELEPHANT_MODEL_LAYER)), 0.75f);
    }

    @Override
    public Identifier getTexture(ElephantRenderState state) {
        return TEXTURE;
    }

    public ElephantRenderState getRenderState() {
        return new ElephantRenderState();
    }

    @Override
    public ElephantRenderState createRenderState() {
        return new ElephantRenderState();
    }
}