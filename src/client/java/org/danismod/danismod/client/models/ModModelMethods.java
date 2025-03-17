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
}
