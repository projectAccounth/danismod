package org.danismod.danismod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import org.danismod.danismod.items.ModItems;

public class Elephant extends PathAwareEntity {

    public Elephant(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this)); // Swim when in water
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25)); // Run away from danger
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0)); // Roam around
        this.goalSelector.add(3, new LookAroundGoal(this)); // Look around idly
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f)); // Look at nearby players6.0F));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(1, new RevengeGoal(this));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 80.0)  // High health
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2) // Normal walking speed
                .add(EntityAttributes.ATTACK_DAMAGE, 9.0)
                .add(EntityAttributes.FOLLOW_RANGE, 20.0) // Strong attack
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 2)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 3);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        LivingEntity target = this.getWorld().getClosestPlayer(this, 10.0);
        if (target != null) {
            this.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
    }


    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean damaged = super.damage(world, source, amount);
        if (damaged && source.getAttacker() instanceof LivingEntity attacker) {
            this.setTarget(attacker);
        }
        return damaged;
    }

    private boolean isAttacking;

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public boolean isAttacking() {
        return this.isAttacking;
    }
}
