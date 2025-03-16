package org.danismod.danismod.client.mobsrenderer.renderstates;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import java.util.Random;

public class LionRenderState extends LivingEntityRenderState {
    private Boolean isMale;

    public LionRenderState() {
        super();
        isMale = new Random().nextBoolean();
    }

    public Boolean isMale() {
        return isMale;
    }
    public void setMaleFlag(Boolean flag) {
        isMale = flag;
    }
}
