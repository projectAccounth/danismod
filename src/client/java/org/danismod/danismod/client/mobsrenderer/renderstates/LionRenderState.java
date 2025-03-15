package org.danismod.danismod.client.mobsrenderer.renderstates;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import java.util.Random;
import java.util.random.RandomGenerator;

public class LionRenderState extends LivingEntityRenderState {
    public boolean isMale;

    public LionRenderState() {
        super();
        isMale = new Random(System.currentTimeMillis()).nextBoolean();
        System.out.println("Lion's appearance: " + (isMale ? "MALE" : "FEMALE"));
    }
}
