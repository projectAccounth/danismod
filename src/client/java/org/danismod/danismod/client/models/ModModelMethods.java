package org.danismod.danismod.client.models;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.MathHelper;

public class ModModelMethods {
    static void animateTail(float limbSwing, float limbAmplitude, ModelPart tail, float maxTailSwing, float gravityEffect) {
        if (limbAmplitude > 0.01F) {
            tail.pitch = MathHelper.sin(limbSwing * 0.5F) * limbAmplitude * 0.4F - gravityEffect;
        } else {
            tail.pitch = -gravityEffect;
        }

        tail.pitch = MathHelper.clamp(tail.pitch, -maxTailSwing, maxTailSwing);
    }

    static void animateLegs(float limbSwing, float limbAmplitude, ModelPart FR, ModelPart FL, ModelPart RR, ModelPart RL) {
        if (limbSwing < 0.01F) {
            FR.pitch = 0.0F;
            FL.pitch = 0.0F;
            RR.pitch = 0.0F;
            RL.pitch = 0.0F;
        } else {
            FR.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
            FL.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
            RR.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbAmplitude;
            RL.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbAmplitude;
        }
    }
}
