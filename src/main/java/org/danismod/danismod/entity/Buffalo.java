package org.danismod.danismod.entity;

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
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import org.danismod.danismod.entity.mob_routines.BuffaloChargeAttackGoal;
import org.danismod.danismod.entity.mob_routines.FollowLeaderGoal;
import org.danismod.danismod.entity.mob_routines.HasLeaderEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// reusing code
public class Buffalo extends AnimalEntity implements HasLeaderEntity<Buffalo> {

    public Buffalo(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this)); // Swim when in water
        // this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4)); // Run away from danger
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.25)); // Breeding behavior
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0)); // Roam around

        this.goalSelector.add(2, new FollowLeaderGoal<>(this, 1.2));

        this.goalSelector.add(2, new LookAroundGoal(this)); // Look around idly
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f)); // Look at players

        this.goalSelector.add(3, new BuffaloChargeAttackGoal(this, 1.9f)); // Charge at godly speed
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 60.0)  // High health
                .add(EntityAttributes.MOVEMENT_SPEED, 0.28) // Normal walking speed
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
            if (attacker instanceof PlayerEntity && world.getDifficulty() == Difficulty.PEACEFUL) 
                return damaged;
            if (attacker.isInCreativeMode() || attacker.isSpectator()) return damaged;

            List<Buffalo> nearbyBuffalos = getNearbyEntities(this);

            for (Buffalo buffalo : nearbyBuffalos)
                if (!buffalo.isBaby()) buffalo.setTarget(attacker);

            if (!this.isBaby()) this.setTarget(attacker);
        }
        return damaged;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (!(target instanceof LivingEntity)) return false;
        LivingEntity livingTarget = (LivingEntity) target;

        if (livingTarget.isDead()) return false;
        if (livingTarget.isInCreativeMode() || livingTarget.isSpectator()) return false;

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
        Buffalo babyBuffalo = ModEntities.BUFFALO.create(world, null, this.getBlockPos(), SpawnReason.BREEDING, false, false);

        if (babyBuffalo != null) {
            babyBuffalo.setBaby(true);
        }

        return babyBuffalo;
    }

    @Override
    public boolean isBreedingItem(@NotNull ItemStack stack) {
        return stack.isOf(Items.HAY_BLOCK);
    }

    public Buffalo groupLeader;
    public final List<Buffalo> groupMembers = new ArrayList<>();

    public boolean hasLeader() {
        return groupLeader != null;
    }

    public void setLeader(Buffalo leader) {
        if (leader == null || !leader.isAlive()) {
            this.setLeader(findNewLeader()); // Assign a valid leader if the previous one is gone
        } else {
            this.groupLeader = leader;
        }
    }

    public List<Buffalo> getNearbyMembers() {
        Buffalo leader = this.getLeader(); // Store leader once to avoid repeated calls

        return this.getWorld().getEntitiesByClass(
                Buffalo.class,
                this.getBoundingBox().expand(30), // 30-block radius
                buffalo -> buffalo != this && buffalo.hasLeader() && buffalo.getLeader() == leader // Check without recursion
        );
    }

    @Nullable
    private Buffalo findNewLeader() {
        if (!this.hasLeader() || this.groupLeader == null || !this.groupLeader.isAlive()) {
            List<Buffalo> groupMembers = this.getNearbyMembers();

            for (Buffalo buffalo : groupMembers) {
                if (!buffalo.isBaby()) {
                    return buffalo;
                }
            }
            if (!groupMembers.isEmpty()) {
                Buffalo newLeader = groupMembers.get(0);
                for (Buffalo buffalo : groupMembers) {
                    if (buffalo.age > newLeader.age) {
                        // re-assigned local variable (checked, shouldn't cause problems)
                        newLeader = buffalo;
                    }
                }
                return newLeader;
            } else {
                return this;
            }
        }
        return getLeader();
    }

    public Buffalo getLeader() {
        if (this.groupLeader != null && this.groupLeader.isAlive()) {
            return this.groupLeader;
        }
        return null; // Prevent recursion - if no leader, return null
    }

    public void addMember(Buffalo member) {
        groupMembers.add(member);
        member.setLeader(this);
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
        List<Buffalo> nearbyBuffalos = getNearbyEntities(this);

        if (nearbyBuffalos.isEmpty()) {
            this.setLeader(this);
        } else {
            Buffalo leader = nearbyBuffalos.get(0).getLeader();
            if (leader == null || !leader.isAlive()) {
                leader = findNewLeader();
            }
            this.setLeader(leader);
            if (leader != null) {
                leader.addMember(this);
            }
        }

        return entityData;
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F; // 50% size for babies
    }
}