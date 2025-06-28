package org.danismod.danismod.entity.mob_routines;

import java.util.EnumSet;

import org.danismod.danismod.entity.mobs.Buffalo;

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
    private int attackCooldown;
    private int postChargeCooldown;
    private boolean isCharging;

    private final int CHARGE_WINDUP = 25; // in ticks (was 15)
    private final int POST_CHARGE_COOLDOWN = 40; // in ticks (2 seconds)
    private final int ATTACK_COOLDOWN = 20; // in ticks (1 second)
    private final double MIN_CHARGE_DIST = 5;
    private final double CLOSE_ATTACK_RANGE = 2.5;

    public BuffaloChargeAttackGoal(Buffalo buffalo, double chargeSpeed) {
        this.buffalo = buffalo;
        this.chargeSpeed = chargeSpeed * 0.7; // reduce speed
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        target = buffalo.getTarget();
        return target != null && target.isAlive() && buffalo.isOnGround() && postChargeCooldown <= 0;
    }

    @Override
    public void start() {
        chargeCooldown = CHARGE_WINDUP;
        attackCooldown = 0;
        isCharging = false;
    }

    @Override
    public void stop() {
        isCharging = false;
        postChargeCooldown = POST_CHARGE_COOLDOWN;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && target.isAlive() && postChargeCooldown <= 0;
    }

    @Override
    public void tick() {
        if (buffalo.getWorld() == null || buffalo.getWorld().isClient) return;
        if (target == null || !target.isAlive()) return;

        if (postChargeCooldown > 0) {
            postChargeCooldown--;
            buffalo.setVelocity(Vec3d.ZERO);
            buffalo.velocityModified = true;
            return;
        }

        if (target.isInCreativeMode() || target.isSpectator()) {
            buffalo.setTarget(null);
            return;
        }

        buffalo.getLookControl().lookAt(target, 360.0F, 360.0F);

        double distanceSq = buffalo.squaredDistanceTo(target);

        // Approach slowly if close, but not in attack range
        if (distanceSq < MIN_CHARGE_DIST * MIN_CHARGE_DIST) {
            Vec3d approachDir = target.getPos().subtract(buffalo.getPos()).normalize();
            buffalo.setVelocity(approachDir.multiply(0.4)); // slower approach
            buffalo.velocityModified = true;

            if (distanceSq < CLOSE_ATTACK_RANGE) {
                if (attackCooldown <= 0) {
                    buffalo.tryAttack((ServerWorld) buffalo.getWorld(), target);
                    attackCooldown = ATTACK_COOLDOWN;
                    stop();
                }
            }
            if (attackCooldown > 0) attackCooldown--;
            return;
        }

        if (chargeCooldown > 0) {
            chargeCooldown--;
            buffalo.setVelocity(Vec3d.ZERO);
            buffalo.velocityModified = true;
            return;
        }

        if (!isCharging && distanceSq >= MIN_CHARGE_DIST * MIN_CHARGE_DIST) {
            isCharging = true;
            buffalo.getWorld().playSound(
                null, buffalo.getBlockPos(),
                SoundEvents.ENTITY_COW_HURT, // placeholder
                SoundCategory.HOSTILE,
                1.0f, 1.0f
            );
        }

        if (isCharging && distanceSq >= MIN_CHARGE_DIST * MIN_CHARGE_DIST) {
            Vec3d dir = target.getPos().subtract(buffalo.getPos()).normalize();
            buffalo.setVelocity(dir.multiply(chargeSpeed));
            buffalo.velocityModified = true;

            buffalo.setYaw((float)(MathHelper.atan2(dir.z, dir.x) * (180F / Math.PI)) - 90F);
            buffalo.setBodyYaw(buffalo.getYaw());
            buffalo.setHeadYaw(buffalo.getYaw());

            if (distanceSq < 3.0 || buffalo.horizontalCollision) {
                if (attackCooldown <= 0) {
                    buffalo.tryAttack(((ServerWorld) buffalo.getWorld()), target);
                    attackCooldown = ATTACK_COOLDOWN;
                    stop();
                }
            }
        }
    }
}