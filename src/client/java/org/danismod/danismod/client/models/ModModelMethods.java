package org.danismod.danismod.client.models;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModModelMethods {
    public static void animateTail(float limbSwing, float limbAmplitude, ModelPart tail, float maxTailSwing, float gravityEffect) {
        if (limbAmplitude > 0.01F) {
            tail.pitch = MathHelper.sin(limbSwing * 0.5F) * limbAmplitude * 0.4F - gravityEffect;
        } else {
            tail.pitch = -gravityEffect;
        }

        tail.pitch = MathHelper.clamp(tail.pitch, -maxTailSwing, maxTailSwing);
    }

    public static void animateLimbs(float limbSwing, float limbAmplitude,
                                    ModelPart frontLeft, ModelPart frontRight,
                                    ModelPart backLeft, ModelPart backRight) {
        if (limbSwing < 0.01F) {
            frontLeft.pitch = 0.0F;
            frontRight.pitch = 0.0F;
            backLeft.pitch = 0.0F;
            backRight.pitch = 0.0F;
        } else {
            frontLeft.pitch  = (float) Math.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
            frontRight.pitch = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 1.4F * limbAmplitude;
            backLeft.pitch   = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 1.4F * limbAmplitude;
            backRight.pitch  = (float) Math.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
        }
    }
}
