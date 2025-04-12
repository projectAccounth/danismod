package org.danismod.danismod.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.danismod.danismod.entity.mob_routines.FollowAdultGoal;

import java.util.List;

public class Elephant extends AnimalEntity {

    public Elephant(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this)); // Swim when in water
        // this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4)); // Run away from danger
        this.goalSelector.add(1, new FollowAdultGoal(this, 1.25)); // Follow adult elephants
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.25)); // Breeding behavior
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0)); // Roam around
        this.goalSelector.add(2, new LookAroundGoal(this)); // Look around idly
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f)); // Look at players

        // Attack only when provoked
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.add(3, new RevengeGoal(this)); // Attacks back when attacked
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 80.0)  // High health
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2) // Normal walking speed
                .add(EntityAttributes.ATTACK_DAMAGE, 26.0)
                .add(EntityAttributes.FOLLOW_RANGE, 20.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 2)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 3);
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
            if (attacker instanceof PlayerEntity && world.getDifficulty() == Difficulty.PEACEFUL) 
                return damaged;
            if (squaredDistanceTo(attacker) > 900.0D) return damaged;

            List<Elephant> nearbyElephants = this.getNearbyElephants();
            for (Elephant elephant : nearbyElephants)
                elephant.setTarget(attacker);
            this.setTarget(attacker);
        }
        return damaged;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return false;

        if (livingTarget.isDead()) return false;
        if (livingTarget.isInCreativeMode() || livingTarget.isSpectator()) return false;
        
        boolean success = super.tryAttack(world, target);
        if (success) {
            target.damage(
                    world,
                    this.getDamageSources().mobAttack(this),
                    (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE)
            );
            this.playSound(SoundEvents.ITEM_WOLF_ARMOR_CRACK);
        }
        return success;
    }

    @Override
    public Elephant createChild(ServerWorld world, PassiveEntity mate) {
        Elephant babyElephant = ModEntities.ELEPHANT.create(world, null, this.getBlockPos(), SpawnReason.BREEDING, false, false);

        if (babyElephant != null) {
            babyElephant.setBaby(true);
        }

        return babyElephant;
    }

    @Override
    public boolean isBreedingItem(@NotNull ItemStack stack) {
        return stack.isOf(Items.APPLE) || stack.isOf(Items.HAY_BLOCK);
    }

    @Override
    public EntityData initialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData
    ) {
        super.initialize(world, difficulty, spawnReason, entityData);

        if (entityData != null) {
            if (this.getRandom().nextFloat() < 0.2F) { // 20% chance for baby
                this.setBaby(true);
            }
        }

        return entityData;
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F; // 50% size for babies
    }

    public List<Elephant> getNearbyElephants() {
        return this.getWorld().getEntitiesByClass(
                Elephant.class,
                this.getBoundingBox().expand(40, 10, 40),
                elephant -> elephant != this
        );
    }
}