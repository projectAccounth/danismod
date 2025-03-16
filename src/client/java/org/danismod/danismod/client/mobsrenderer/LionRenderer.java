package org.danismod.danismod.client.mobsrenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.Identifier;
import org.danismod.danismod.client.mobsrenderer.renderstates.LionRenderState;
import org.danismod.danismod.client.models.LionModel;
import org.danismod.danismod.client.models.ModModelLayers;
import org.danismod.danismod.entity.Lion;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class LionRenderer extends MobEntityRenderer<Lion, LionRenderState, EntityModel<LionRenderState>> {
    private static final Identifier TEXTURE = Identifier.of("danismod", "textures/entities/lion_norm.png");
    private static final Identifier S_TEXTURE = Identifier.of("danismod", "textures/entities/lion_sleeping.png");

    private static LionModel STANDING_MODEL;
    private static LionModel RESTING_MODEL;

    @Override
    public void updateRenderState(Lion lion, LionRenderState state, float tickDelta) {
        super.updateRenderState(lion, state, tickDelta);
        state.setMaleFlag(lion.isMale());
    }

    public LionRenderer(EntityRendererFactory.Context context) {
        super(context, new LionModel(context.getPart(ModModelLayers.NORMAL_LION_MODEL_LAYER)), 0.75f);
        STANDING_MODEL = new LionModel(context.getPart(ModModelLayers.NORMAL_LION_MODEL_LAYER));
        RESTING_MODEL = new LionModel(context.getPart(ModModelLayers.RESTING_LION_MODEL_LAYER));
    }

    @Override
    public Identifier getTexture(LionRenderState state) {
        return (!state.isInPose(EntityPose.CROUCHING) ? TEXTURE : S_TEXTURE);
    }

    @Override
    public LionRenderState createRenderState() {
        return new LionRenderState();
    }

    @Override
    public void render(@NotNull LionRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (state.isInPose(EntityPose.CROUCHING) || state.isInPose(EntityPose.SLEEPING)) {
            this.model = RESTING_MODEL;
        } else {
            this.model = STANDING_MODEL;
        }

        this.model.getPart("manebone").ifPresent((part) -> {
            part.visible = state.isMale();
        });

        float scale = state.baby ? 0.5F : 1.0F;
        matrices.scale(scale, scale, scale);
        super.render(state, matrices, vertexConsumers, light);
    }
}
