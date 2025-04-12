package org.danismod.danismod.entity.mob_routines;

import java.util.EnumSet;

import org.danismod.danismod.entity.Buffalo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;

public class BuffaloChargeAttackGoal extends Goal {
    private final Buffalo buffalo;
    private LivingEntity target;
    private final double chargeSpeed;
    private int chargeCooldown;
    private boolean isCharging;

    public BuffaloChargeAttackGoal(Buffalo buffalo, double chargeSpeed) {
        this.buffalo = buffalo;
        this.chargeSpeed = chargeSpeed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        target = buffalo.getTarget();
        return target != null && target.isAlive() && buffalo.isOnGround();
    }

    @Override
    public void start() {
        chargeCooldown = 8; // Wind-up before charging (1 sec)
        isCharging = false;
    }

    @Override
    public void stop() {
        isCharging = false;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        if (buffalo.getWorld() == null || ((ServerWorld) buffalo.getWorld()) == null) return;
        if (target.isInCreativeMode() || target.isSpectator()) {
            buffalo.setTarget(null);
            return;
        }
        buffalo.getLookControl().lookAt(target, 90.0F, 90.0F);

        if (chargeCooldown > 0) {
            chargeCooldown--;
            buffalo.getNavigation().stop();
            // Play charging animation (maybe later)
        } else {
            if (!isCharging) {
                isCharging = true;
                buffalo.getNavigation().startMovingTo(target, chargeSpeed);
                // What sound should I put here?
            }

            if (buffalo.squaredDistanceTo(target) < 3.0D) {
                buffalo.tryAttack(((ServerWorld) buffalo.getWorld()), target);
                stop(); // End the goal after hitting the target
            }
        }
    }
}