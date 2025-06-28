package org.danismod.danismod.entity.mobs;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import org.danismod.danismod.entity.ModEntities;
import org.danismod.danismod.entity.mob_routines.BuffaloChargeAttackGoal;
import org.danismod.danismod.entity.mob_routines.FollowLeaderGoal;
import org.jetbrains.annotations.NotNull;

public class Buffalo extends HasLeaderEntity<Buffalo> {

    public Buffalo(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.25));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(2, new FollowLeaderGoal<>(this, 1.2, 5.0));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(3, new BuffaloChargeAttackGoal(this, 1.9f));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 60.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.ATTACK_DAMAGE, 23.0)
                .add(EntityAttributes.FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.5)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 2.4);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        LivingEntity target = this.getWorld().getClosestPlayer(this, 15.0);
        if (target != null) {
            this.getLookControl().lookAt(target, 45.0F, 45.0F);
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean damaged = super.damage(world, source, amount);
        if (damaged && source.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.isInCreativeMode() || attacker.isSpectator()) return damaged;
            if (attacker instanceof PlayerEntity && world.getDifficulty() == Difficulty.PEACEFUL) return damaged;
            if (squaredDistanceTo(attacker) > 900.0D) return damaged;

            for (Buffalo buffalo : getNearbyEntities(this)) {
                if (!buffalo.isBaby()) buffalo.setTarget(attacker);
            }

            if (!this.isBaby()) this.setTarget(attacker);
        }
        return damaged;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return false;
        if (livingTarget.isDead() || livingTarget.isInCreativeMode() || livingTarget.isSpectator()) return false;

        boolean success = super.tryAttack(world, target);
        if (success) {
            target.damage(
                world,
                this.getDamageSources().mobAttack(this),
                (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE)
            );
            livingTarget.takeKnockback(1.2F, MathHelper.sin(this.getYaw() * 0.0175F), -MathHelper.cos(this.getYaw() * 0.0175F));
            this.playSound(SoundEvents.ITEM_WOLF_ARMOR_CRACK);
        }
        return success;
    }

    @Override
    public Buffalo createChild(ServerWorld world, PassiveEntity mate) {
        Buffalo baby = ModEntities.BUFFALO.create(world, null, this.getBlockPos(), SpawnReason.BREEDING, false, false);
        if (baby != null) baby.setBaby(true);
        return baby;
    }

    @Override
    public boolean isBreedingItem(@NotNull ItemStack stack) {
        return stack.isOf(Items.HAY_BLOCK);
    }

    @Override
    protected Buffalo self() {
        return this;
    }
}