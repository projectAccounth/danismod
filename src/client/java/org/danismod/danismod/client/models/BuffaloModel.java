package org.danismod.danismod.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;
import org.danismod.danismod.client.mobsrenderer.renderstates.BuffaloRenderState;

public class BuffaloModel extends EntityModel<BuffaloRenderState> {
	private final ModelPart root;
	private final ModelPart legs;
	private final ModelPart front_left;
	private final ModelPart back_right;
	private final ModelPart back_left;
	private final ModelPart front_right;
	private final ModelPart torso;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart horns;
	private final ModelPart horn_left;
	private final ModelPart horn_right;
	private final ModelPart headpart;
	public BuffaloModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.legs = this.root.getChild("legs");
		this.front_left = this.legs.getChild("front_left");
		this.back_right = this.legs.getChild("back_right");
		this.back_left = this.legs.getChild("back_left");
		this.front_right = this.legs.getChild("front_right");
		this.torso = this.root.getChild("torso");
		this.tail = this.root.getChild("tail");
		this.head = this.root.getChild("head");
		this.horns = this.head.getChild("horns");
		this.horn_left = this.horns.getChild("horn_left");
		this.horn_right = this.horns.getChild("horn_right");
		this.headpart = this.head.getChild("headpart");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.25F, 3.25F));

		ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(-4.75F, 21.75F, -13.25F));

		ModelPartData front_left = legs.addChild("front_left", ModelPartBuilder.create().uv(2, 112).cuboid(-2.5F, -1.0F, -1.0F, 5.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(10.0F, -13.0F, 0.0F));

		ModelPartData back_right = legs.addChild("back_right", ModelPartBuilder.create().uv(76, 77).cuboid(-2.8F, -0.25F, -3.5F, 5.0F, 13.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -12.75F, 26.5F));

		ModelPartData back_left = legs.addChild("back_left", ModelPartBuilder.create().uv(43, 108).cuboid(-2.45F, 0.0F, -3.5F, 5.0F, 13.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(9.0F, -13.0F, 26.5F));

		ModelPartData front_right = legs.addChild("front_right", ModelPartBuilder.create().uv(78, 0).cuboid(-2.5F, -0.75F, -3.0F, 5.0F, 14.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, -13.25F, 2.0F));

		ModelPartData torso = root.addChild("torso", ModelPartBuilder.create().uv(4, 0).cuboid(-2.0F, -16.0F, -31.0F, 16.0F, 17.0F, 19.0F, new Dilation(0.0F))
		.uv(72, 58).cuboid(-1.0F, 2.0F, -12.0F, 14.0F, 3.0F, 16.0F, new Dilation(0.0F))
		.uv(72, 36).cuboid(-2.0F, 1.0F, -31.0F, 16.0F, 3.0F, 19.0F, new Dilation(0.0F))
		.uv(4, 36).cuboid(-1.0F, -15.0F, -12.0F, 14.0F, 17.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, 6.75F, 11.75F));

		ModelPartData tail = root.addChild("tail", ModelPartBuilder.create().uv(66, 85).cuboid(-1.0F, -1.0F, 17.25F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
		.uv(78, 20).cuboid(-0.5F, -0.5F, 0.25F, 1.0F, 1.0F, 13.0F, new Dilation(0.0F))
		.uv(40, 69).cuboid(-1.0F, -1.0F, 13.25F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.75F, 15.5F));

		ModelPartData head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -5.25F, -19.0F));

		ModelPartData horns = head.addChild("horns", ModelPartBuilder.create(), ModelTransform.pivot(-8.924F, -1.4266F, -1.9128F));

		ModelPartData horn_left = horns.addChild("horn_left", ModelPartBuilder.create(), ModelTransform.pivot(17.8481F, 0.0F, 0.0F));

		ModelPartData cube_r1 = horn_left.addChild("cube_r1", ModelPartBuilder.create().uv(144, 150).cuboid(0.9F, -6.7F, -3.5F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 152).cuboid(-9.1F, -0.7F, -3.5F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3978F, -0.503F, 0.3132F));

		ModelPartData horn_right = horns.addChild("horn_right", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r2 = horn_right.addChild("cube_r2", ModelPartBuilder.create().uv(0, 92).cuboid(-4.9F, -6.7F, -3.5F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(40, 77).cuboid(-4.9F, -0.7F, -3.5F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3978F, 0.503F, -0.3132F));

		ModelPartData headpart = head.addChild("headpart", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.4235F, -3.024F));

		ModelPartData cube_r3 = headpart.addChild("cube_r3", ModelPartBuilder.create().uv(52, 69).cuboid(-8.0F, 0.0F, -1.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(3, 6).mirrored().cuboid(4.0F, 0.0F, -1.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
		.uv(3, 72).cuboid(-4.0F, -5.5F, -5.0F, 8.0F, 10.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r4 = headpart.addChild("cube_r4", ModelPartBuilder.create().uv(40, 85).cuboid(-3.0F, -1.0F, -1.0F, 6.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.0974F, -8.5855F, 0.8901F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void setAngles(BuffaloRenderState state) {
		float maxHeadYaw = 45F * ((float) Math.PI / 180F); // Max head rotation in radians
		float headPitchTarget = MathHelper.clamp(state.pitch, -20F, 20F) * ((float) Math.PI / 180F);

		// Smooth interpolation for natural movement
		this.head.yaw = MathHelper.clamp(state.bodyYaw - maxHeadYaw, -maxHeadYaw, maxHeadYaw) * 0.2F;
		this.head.pitch = (headPitchTarget - this.head.pitch) * 0.2F;

		// Keep existing limb animation
		float limbSwing = state.limbFrequency;
		float limbAmplitude = state.limbAmplitudeMultiplier;

		if (limbSwing < 0.01F) {
			this.front_right.pitch = 0.0F;
			this.front_left.pitch = 0.0F;
			this.back_right.pitch = 0.0F;
			this.back_left.pitch = 0.0F;
		} else {
			this.front_right.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
			this.front_left.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
			this.back_right.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
			this.back_left.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
		}

		// Adjust tail movement naturally
		ModModelMethods.animateTail(limbSwing, limbAmplitude, this.tail, 0.8F, 0.3F);
	}
}