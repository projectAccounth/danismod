package org.danismod.danismod.client.mobsrenderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.danismod.danismod.client.mobsrenderer.renderstates.BuffaloRenderState;
import org.danismod.danismod.client.models.BuffaloModel;
import org.danismod.danismod.client.models.ModModelLayers;
import org.danismod.danismod.entity.Buffalo;
import org.jetbrains.annotations.NotNull;

public class BuffaloRenderer extends MobEntityRenderer<Buffalo, BuffaloRenderState, BuffaloModel> {
    private static final Identifier TEXTURE = Identifier.of("danismod", "textures/entities/buffalo.png");

    public BuffaloRenderer(EntityRendererFactory.Context context) {
        super(context, new BuffaloModel(context.getPart(ModModelLayers.BUFFALO_MODEL_LAYER)), 0.75f);
    }

    @Override
    public Identifier getTexture(BuffaloRenderState state) {
        return TEXTURE;
    }

    @Override
    public BuffaloRenderState createRenderState() {
        return new BuffaloRenderState();
    }

    @Override
    public void render(@NotNull BuffaloRenderState state, @NotNull MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float scale = state.baby ? 0.5F : 1.0F;
        this.model.getPart("horns").ifPresent((part) -> part.visible = !state.baby);
        matrices.scale(scale, scale, scale);
        super.render(state, matrices, vertexConsumers, light);
    }
}
