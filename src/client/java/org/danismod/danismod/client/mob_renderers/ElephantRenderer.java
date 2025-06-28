package org.danismod.danismod.client.mob_renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

import org.danismod.danismod.Danismod;
import org.danismod.danismod.client.mob_renderers.render_states.ElephantRenderState;
import org.danismod.danismod.client.models.ModModelLayers;
import org.danismod.danismod.client.models.entities.ElephantModel;
import org.danismod.danismod.entity.mobs.Elephant;

import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRendererFactory;

@Environment(EnvType.CLIENT)
public class ElephantRenderer extends MobEntityRenderer<Elephant, ElephantRenderState, ElephantModel> {
    private static final Identifier TEXTURE = Identifier.of(Danismod.MOD_ID, "textures/entities/elephant.png");

    public ElephantRenderer(EntityRendererFactory.Context context) {
        super(context, new ElephantModel(context.getPart(ModModelLayers.ELEPHANT_MODEL_LAYER)), 0.75f);
    }

    @Override
    public Identifier getTexture(ElephantRenderState state) {
        return TEXTURE;
    }

    @Override
    public ElephantRenderState createRenderState() {
        return new ElephantRenderState();
    }

    @Override
    public void render(ElephantRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float scale = state.baby ? 0.5F : 1.0F;
        matrices.scale(scale, scale, scale);
        super.render(state, matrices, vertexConsumers, light);
    }
}