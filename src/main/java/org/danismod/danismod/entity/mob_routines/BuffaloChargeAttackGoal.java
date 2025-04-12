package org.danismod.danismod.entity.mob_routines;

import java.util.EnumSet;

import org.danismod.danismod.entity.Buffalo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        chargeCooldown = 15; // Wind-up before charging (.75 sec)
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
        if (buffalo.getWorld() == null || buffalo.getWorld().isClient) return;
        if (target == null || !target.isAlive()) return;

        if (target.isInCreativeMode() || target.isSpectator()) {
            buffalo.setTarget(null);
            return;
        }

        buffalo.getLookControl().lookAt(target, 360.0F, 360.0F);

        double distanceSq = buffalo.squaredDistanceTo(target);

        if (distanceSq < 25.0) {
            Vec3d approachDir = target.getPos().subtract(buffalo.getPos()).normalize();
            buffalo.setVelocity(approachDir.multiply(0.8));
            buffalo.velocityModified = true;

            if (distanceSq < 2.5) {
                buffalo.tryAttack((ServerWorld) buffalo.getWorld(), target);
                stop();
            }
            return;
        }

        if (chargeCooldown > 0) {
            chargeCooldown--;
            buffalo.setVelocity(Vec3d.ZERO);
            buffalo.velocityModified = true;
            return;
        }

        if (!isCharging && distanceSq >= 25.0) {
            isCharging = true;

            // TODO: add charge sound
            buffalo.getWorld().playSound(
                null, buffalo.getBlockPos(),
                SoundEvents.ENTITY_COW_HURT, // placeholder
                SoundCategory.HOSTILE,
                1.0f, 1.0f
            );
        }

        if (isCharging && distanceSq >= 25.0) {
            Vec3d dir = target.getPos().subtract(buffalo.getPos()).normalize();
            buffalo.setVelocity(dir.multiply(chargeSpeed));
            buffalo.velocityModified = true;

            buffalo.setYaw((float)(MathHelper.atan2(dir.z, dir.x) * (180F / Math.PI)) - 90F);
            buffalo.setBodyYaw(buffalo.getYaw());
            buffalo.setHeadYaw(buffalo.getYaw());

            if (distanceSq < 3.0 || buffalo.horizontalCollision) {
                buffalo.tryAttack(((ServerWorld) buffalo.getWorld()), target);
                stop();
            }
        }
    }
}