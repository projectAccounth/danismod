package org.danismod.danismod.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.MathHelper;
import org.danismod.danismod.client.mobsrenderer.renderstates.LionRenderState;
import org.jetbrains.annotations.NotNull;

public class LionModel extends EntityModel<LionRenderState> {
    private final ModelPart leg1bone;
	private final ModelPart leg2bone;
	private final ModelPart leg3bone;
	private final ModelPart leg4bone;
	private final ModelPart headbone;
    private final ModelPart tailbone;

	public LionModel(ModelPart root) {
		super(root);
        ModelPart root1 = root.getChild("root");
        ModelPart legs = root1.getChild("legs");
		this.leg1bone = legs.getChild("leg1bone");
		this.leg2bone = legs.getChild("leg2bone");
		this.leg3bone = legs.getChild("leg3bone");
		this.leg4bone = legs.getChild("leg4bone");
		this.headbone = root1.getChild("headbone");
        ModelPart snoutbone = this.headbone.getChild("snoutbone");
        ModelPart ears = this.headbone.getChild("ears");
        ModelPart ear1bone = ears.getChild("ear1bone");
        ModelPart ear2bone = ears.getChild("ear2bone");
		ModelPart manebone = this.headbone.getChild("manebone");
		this.tailbone = root1.getChild("tailbone");
	}
	@NotNull
	public static TexturedModelData getStandingModel() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -5.0F, -12.0F, 10.0F, 10.0F, 24.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 9.0F, -1.0F));

		ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 9.0F, 1.0F));

		ModelPartData leg1bone = legs.addChild("leg1bone", ModelPartBuilder.create().uv(38, 66).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -4.0F, -10.0F));

		ModelPartData leg2bone = legs.addChild("leg2bone", ModelPartBuilder.create().uv(54, 66).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -4.0F, -10.0F));

		ModelPartData leg3bone = legs.addChild("leg3bone", ModelPartBuilder.create().uv(22, 54).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(3.25F, -4.0F, 11.0F));

		ModelPartData leg4bone = legs.addChild("leg4bone", ModelPartBuilder.create().uv(0, 66).cuboid(-2.0F, -2.25F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.25F, -3.75F, 11.0F));

		ModelPartData headbone = root.addChild("headbone", ModelPartBuilder.create().uv(40, 52).cuboid(-4.0F, -3.5F, -7.25F, 8.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, -11.75F));

		ModelPartData snoutbone = headbone.addChild("snoutbone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.3229F, -7.3461F));

		ModelPartData snout_r1 = snoutbone.addChild("snout_r1", ModelPartBuilder.create().uv(0, 54).cuboid(-2.0F, -1.55F, -3.5F, 4.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.4229F, 0.5961F, 0.3491F, 0.0F, 0.0F));

		ModelPartData ears = headbone.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, -2.0F, -3.75F));

		ModelPartData ear1bone = ears.addChild("ear1bone", ModelPartBuilder.create(), ModelTransform.pivot(7.2145F, -1.1651F, 0.0F));

		ModelPartData ear1_r1 = ear1bone.addChild("ear1_r1", ModelPartBuilder.create().uv(68, 0).cuboid(-1.5F, -1.25F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0855F, -0.2349F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData ear2bone = ears.addChild("ear2bone", ModelPartBuilder.create(), ModelTransform.pivot(-0.3145F, -1.1651F, 0.0F));

		ModelPartData ear2_r1 = ear2bone.addChild("ear2_r1", ModelPartBuilder.create().uv(68, 4).cuboid(-1.5F, -1.25F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0855F, -0.2349F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData manebone = headbone.addChild("manebone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.333F, -0.551F));

		ModelPartData mane_r1 = manebone.addChild("mane_r1", ModelPartBuilder.create().uv(0, 34).cuboid(-6.0F, -5.75F, -3.5F, 12.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.333F, -0.449F, -0.1745F, 0.0F, 0.0F));

		ModelPartData tailbone = root.addChild("tailbone", ModelPartBuilder.create().uv(43, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F))
		.uv(61, 44).cuboid(-1.0F, -1.0F, 13.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 12.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@NotNull
	public static TexturedModelData getLyingModel() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -5.0F, -12.0F, 10.0F, 10.0F, 24.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 17.25F, -1.0F));

		ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 9.0F, 1.0F));

		ModelPartData leg1bone = legs.addChild("leg1bone", ModelPartBuilder.create().uv(38, 66).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -4.0F, -10.0F, -1.5244F, -0.3487F, -0.0159F));

		ModelPartData leg2bone = legs.addChild("leg2bone", ModelPartBuilder.create().uv(54, 66).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -4.0F, -10.0F, -1.5244F, 0.3487F, 0.0159F));

		ModelPartData leg3bone = legs.addChild("leg3bone", ModelPartBuilder.create().uv(22, 54).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.25F, -4.0F, 11.0F, 1.3902F, 0.2577F, -0.0465F));

		ModelPartData leg4bone = legs.addChild("leg4bone", ModelPartBuilder.create().uv(0, 66).cuboid(-2.0F, -2.25F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-3.25F, -3.75F, 11.0F, 1.3902F, -0.2577F, 0.0465F));

		ModelPartData headbone = root.addChild("headbone", ModelPartBuilder.create().uv(40, 52).cuboid(-4.0F, -3.5F, -7.25F, 8.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, -11.75F));

		ModelPartData snoutbone = headbone.addChild("snoutbone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.3229F, -7.3461F));

		ModelPartData snout_r1 = snoutbone.addChild("snout_r1", ModelPartBuilder.create().uv(0, 54).cuboid(-2.0F, -1.55F, -3.5F, 4.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.4229F, 0.5961F, 0.3491F, 0.0F, 0.0F));

		ModelPartData ears = headbone.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, -2.0F, -3.75F));

		ModelPartData ear1bone = ears.addChild("ear1bone", ModelPartBuilder.create(), ModelTransform.pivot(7.2145F, -1.1651F, 0.0F));

		ModelPartData ear1_r1 = ear1bone.addChild("ear1_r1", ModelPartBuilder.create().uv(68, 0).cuboid(-1.5F, -1.25F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0855F, -0.2349F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData ear2bone = ears.addChild("ear2bone", ModelPartBuilder.create(), ModelTransform.pivot(-0.3145F, -1.1651F, 0.0F));

		ModelPartData ear2_r1 = ear2bone.addChild("ear2_r1", ModelPartBuilder.create().uv(68, 4).cuboid(-1.5F, -1.25F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0855F, -0.2349F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData manebone = headbone.addChild("manebone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.333F, -0.551F));

		ModelPartData mane_r1 = manebone.addChild("mane_r1", ModelPartBuilder.create().uv(0, 34).cuboid(-6.0F, -5.75F, -3.5F, 12.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.333F, -0.449F, -0.1745F, 0.0F, 0.0F));

		ModelPartData tailbone = root.addChild("tailbone", ModelPartBuilder.create().uv(43, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F))
				.uv(61, 44).cuboid(-1.0F, -1.0F, 13.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 12.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(@NotNull LionRenderState state) {
		if (state.pose != EntityPose.STANDING) return;

		float maxHeadYaw = 45F * ((float) Math.PI / 180F); // Max head rotation in radians
		float headPitchTarget = MathHelper.clamp(state.pitch, -20F, 20F) * ((float) Math.PI / 180F);

		// Smooth interpolation for natural movement
		this.headbone.yaw = MathHelper.clamp(state.bodyYaw - maxHeadYaw, -maxHeadYaw, maxHeadYaw) * 0.2F;
		this.headbone.pitch = (headPitchTarget - this.headbone.pitch) * 0.2F;

		// Keep existing limb animation
		float limbSwing = state.limbFrequency;
		float limbAmplitude = state.limbAmplitudeMultiplier;

		if (limbSwing < 0.01F) {
			this.leg2bone.pitch = 0.0F;
			this.leg1bone.pitch = 0.0F;
			this.leg4bone.pitch = 0.0F;
			this.leg3bone.pitch = 0.0F;
		} else {
			// front-right
			this.leg2bone.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
			// front-left
			this.leg1bone.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
			// back-right
			this.leg4bone.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
			// back-left
			this.leg3bone.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
		}

		// Adjust tail movement naturally
		ModModelMethods.animateTail(limbSwing, limbAmplitude, this.tailbone, 0.8F, 0.3F);
	}
}