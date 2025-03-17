package org.danismod.danismod.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.danismod.danismod.client.mobsrenderer.renderstates.ElephantRenderState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ElephantModel extends EntityModel<ElephantRenderState> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart frontleft;
    private final ModelPart backright;
    private final ModelPart frontright;
    private final ModelPart backleft;
    private final ModelPart tail;
    public ElephantModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        ModelPart torso = this.root.getChild("torso");
        this.head = this.root.getChild("head");
        ModelPart ears = this.head.getChild("ears");
        ModelPart earRight = ears.getChild("earRight");
        ModelPart earLeft = ears.getChild("earLeft");
        ModelPart trunk = this.head.getChild("trunk");
        ModelPart tusks = this.head.getChild("tusks");
        ModelPart tuskLeft = tusks.getChild("tuskLeft");
        ModelPart tuskRight = tusks.getChild("tuskRight");
        ModelPart legs = this.root.getChild("legs");
        this.frontleft = legs.getChild("frontleft");
        this.backright = legs.getChild("backright");
        this.frontright = legs.getChild("frontright");
        this.backleft = legs.getChild("backleft");
        this.tail = this.root.getChild("tail");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(),
                ModelTransform.of(-0.8F, -3.0F, 2.5F, 0.0F, (float)Math.PI, 0.0F)
        );

        ModelPartData torso = root.addChild("torso", ModelPartBuilder.create().uv(8, 0).cuboid(-20.6F, 0.0F, -16.7F, 21.8F, 19.0F, 36.7F, new Dilation(0.0F))
                .uv(150, 17).cuboid(-17.6F, -2.8F, -16.7F, 16.0F, 2.8F, 36.7F, new Dilation(0.0F)), ModelTransform.pivot(10.6F, -12.0F, -4.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(89, 11).cuboid(-5.7518F, -7.2397F, 0.2797F, 11.5518F, 14.4397F, 11.5518F, new Dilation(0.0F)), ModelTransform.pivot(0.7F, -9.4828F, 16.0203F));

        ModelPartData ears = head.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(-0.1F, 1.5F, 1.4F));

        ModelPartData earRight = ears.addChild("earRight", ModelPartBuilder.create().uv(37, 56).cuboid(16.6F, -10.1276F, -5.7203F, 13.973F, 17.3276F, 1.0397F, new Dilation(0.0F)), ModelTransform.pivot(-11.0F, 0.0F, 7.0F));

        ModelPartData earLeft = ears.addChild("earLeft", ModelPartBuilder.create().uv(6, 56).cuboid(-8.173F, -10.1276F, -5.7203F, 13.973F, 17.3276F, 1.0397F, new Dilation(0.0F)), ModelTransform.pivot(-11.0F, 0.0F, 7.0F));

        ModelPartData trunk = head.addChild("trunk", ModelPartBuilder.create().uv(124, 7).cuboid(0.9698F, 10.3921F, -2.1203F, 3.6802F, 12.2079F, 4.0891F, new Dilation(0.0F))
                .uv(68, 58).cuboid(0.4498F, -0.1515F, -2.7203F, 4.6002F, 10.8515F, 5.1113F, new Dilation(0.0F))
                .uv(126, 38).cuboid(-0.0875F, -11.8572F, -3.3203F, 5.7875F, 12.0572F, 6.4305F, new Dilation(0.0F)), ModelTransform.pivot(-2.7F, 9.8F, 13.2F));

        ModelPartData tusks = head.addChild("tusks", ModelPartBuilder.create(), ModelTransform.pivot(-0.1891F, 6.2484F, 10.878F));

        ModelPartData tuskLeft = tusks.addChild("tuskLeft", ModelPartBuilder.create(), ModelTransform.pivot(-3.4218F, 0.0F, 0.0F));

        ModelPartData cube_r1 = tuskLeft.addChild("cube_r1", ModelPartBuilder.create().uv(89, 59).cuboid(-1.6435F, 6.8574F, 0.1705F, 2.3869F, 13.4852F, 2.4589F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.1435F, -6.7426F, -0.3295F, 3.5869F, 13.5852F, 3.4589F, new Dilation(0.0F))
                .uv(100, 62).cuboid(-1.6435F, 17.8574F, 0.1705F, 2.3869F, 2.4852F, 10.6589F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.4861F, -0.1574F, 0.1147F));

        ModelPartData tuskRight = tusks.addChild("tuskRight", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = tuskRight.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.4434F, -6.7426F, -0.3295F, 3.5869F, 13.5852F, 3.4589F, new Dilation(0.0F))
                .uv(100, 62).cuboid(-0.7435F, 17.8574F, 0.1705F, 2.3869F, 2.4852F, 10.6589F, new Dilation(0.0F))
                .uv(89, 59).cuboid(-0.7435F, 6.8574F, 0.1705F, 2.3869F, 13.4852F, 2.4589F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.4861F, 0.1574F, -0.1147F));

        ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(1.0875F, 6.0384F, -2.8417F));

        ModelPartData frontleft = legs.addChild("frontleft", ModelPartBuilder.create().uv(14, 0).cuboid(-3.8725F, 0.766F, -3.8583F, 7.425F, 19.9233F, 7.7166F, new Dilation(0.0F)), ModelTransform.pivot(-6.3F, 0.0F, 14.0F));

        ModelPartData backright = legs.addChild("backright", ModelPartBuilder.create().uv(14, 0).cuboid(-3.8725F, 0.766F, -3.8583F, 7.425F, 19.9233F, 7.7166F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, 0.0F, -13.0F));

        ModelPartData frontright = legs.addChild("frontright", ModelPartBuilder.create().uv(14, 0).cuboid(8.4275F, 0.766F, -3.8583F, 7.425F, 19.9233F, 7.7166F, new Dilation(0.0F)), ModelTransform.pivot(-6.3F, 0.0F, 14.0F));

        ModelPartData backleft = legs.addChild("backleft", ModelPartBuilder.create().uv(14, 0).cuboid(-5.1725F, 2.766F, -3.8583F, 7.425F, 19.9233F, 7.7166F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -2.0F, -13.0F));

        ModelPartData tail = root.addChild("tail", ModelPartBuilder.create().uv(118, 65).cuboid(-1.1334F, -1.127F, -13.5477F, 2.2334F, 2.3049F, 3.7681F, new Dilation(0.0F))
                .uv(102, 44).cuboid(-0.6138F, -0.6779F, -9.7477F, 1.3138F, 1.3558F, 9.8953F, new Dilation(0.0F)), ModelTransform.pivot(0.8F, -6.5779F, -20.5524F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void setAngles(@NotNull ElephantRenderState state) {
        float maxHeadYaw = 45F * ((float) Math.PI / 180F); // Max head rotation in radians
        float headPitchTarget = MathHelper.clamp(state.pitch, -20F, 20F) * ((float) Math.PI / 180F);

        // Smooth interpolation for natural movement
        this.head.yaw = MathHelper.clamp(state.bodyYaw - maxHeadYaw, -maxHeadYaw, maxHeadYaw) * 0.2F;
        this.head.pitch = (headPitchTarget - this.head.pitch) * 0.2F;

        // Keep existing limb animation
        float limbSwing = state.limbFrequency;
        float limbAmplitude = state.limbAmplitudeMultiplier;

        if (limbSwing < 0.01F) {
            this.frontright.pitch = 0.0F;
            this.frontleft.pitch = 0.0F;
            this.backright.pitch = 0.0F;
            this.backleft.pitch = 0.0F;
        } else {
            this.frontright.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
            this.frontleft.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
            this.backright.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
            this.backleft.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
        }

        // Adjust tail movement naturally
        ModModelMethods.animateTail(limbSwing, limbAmplitude, this.tail, -0.8F, -0.3F);
    }
}
