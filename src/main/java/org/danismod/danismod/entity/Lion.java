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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import org.danismod.danismod.entity.mob_routines.FollowLeaderGoal;
import org.danismod.danismod.entity.mob_routines.HasLeaderEntity;
import org.danismod.danismod.entity.mob_routines.LionRoutineGoal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Lion extends AnimalEntity implements HasLeaderEntity<Lion> {
    public Lion prideLeader;
    public final List<Lion> prideMembers = new ArrayList<>();

    private Boolean isMale = this.getRandom().nextFloat() < 0.5F;

    public boolean hasLeader() {
        return prideLeader != null;
    }

    public void setLeader(Lion leader) {
        if (leader == null || !leader.isAlive()) {
            this.setLeader(findNewLeader()); // Assign a valid leader if the previous one is gone
        } else {
            this.prideLeader = leader;
        }
    }

    @Nullable
    private Lion findNewLeader() {
        if (!this.hasLeader() || this.prideLeader == null || !this.prideLeader.isAlive()) {
            List<Lion> prideMembers = getNearbyEntities(this);

            // Prioritize an adult lion
            for (Lion lion : prideMembers) {
                if (!lion.isBaby()) {
                    return lion;
                }
            }

            // If no adults exist, pick the oldest lion
            if (!prideMembers.isEmpty()) {
                Lion newLeader = prideMembers.get(0);
                for (Lion lion : prideMembers) {
                    if (lion.age > newLeader.age) {
                        newLeader = lion;
                    }
                }
                return newLeader;
            } else {
                return this;
            }
        }
        return null;
    }

    public Lion getLeader() {
        if (this.prideLeader != null && this.prideLeader.isAlive()) {
            return this.prideLeader;
        }
        return null; // Prevent recursion - if no leader, return null
    }

    public void addPrideMember(Lion member) {
        prideMembers.add(member);
        member.setLeader(this);
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldAccess, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData) {
        super.initialize(worldAccess, difficulty, spawnReason, entityData);

        if (entityData != null) {
            if (this.getRandom().nextFloat() < 0.2F) { // 20% chance for baby
                this.setBaby(true);
            }
        }

        isMale = this.getRandom().nextFloat() < 0.5F;
        System.out.println("New lion's gender: " + (isMale ? "Male" : "Female"));

        // Find nearby lions to form a pride
        List<Lion> nearbyLions = worldAccess.toServerWorld().getEntitiesByClass(
                Lion.class, this.getBoundingBox().expand(70), lion -> lion != this
        );

        if (nearbyLions.isEmpty()) {
            this.setLeader(this); // If no leader exists, become one
        } else {
            Lion leader = nearbyLions.get(0).getLeader();
            if (leader == null || !leader.isAlive()) {
                leader = findNewLeader();
            }
            this.setLeader(leader);
            if (leader != null) {
                leader.addPrideMember(this);
                System.out.println("Found a new pride for newly spawned lion!");
            }
        }

        return entityData;
    }

    public Lion(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this)); // Swim when in water
        // this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4)); // Shouldn't be scared

        this.goalSelector.add(1, new LionRoutineGoal(this)); // Custom AI for roaming, hunting, sleeping
        this.goalSelector.add(2, new FollowLeaderGoal<>(this, 1.2)); // Follow leader if in a pride

        this.goalSelector.add(3, new AnimalMateGoal(this, 1.25)); // Breeding behavior
        // this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0)); // Roam when not in routine
        this.goalSelector.add(3, new LookAroundGoal(this)); // Look around randomly

        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f)); // Look at players

        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.2, false)); // Attack prey or threats
        this.goalSelector.add(5, new RevengeGoal(this)); // Attack back when provoked
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 30.0)
                .add(EntityAttributes.MOVEMENT_SPEED, .3)
                .add(EntityAttributes.ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.FOLLOW_RANGE, 20.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, .2)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 1);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        LivingEntity target = this.getWorld().getClosestPlayer(this, 15.0);
        if (target != null) {
            this.getLookControl().lookAt(target, 45.0F, 45.0F);
        }
        if (this.isTouchingWater()) {
            Vec3d escapeDirection = this.getPos().subtract(0, 0, 1).normalize();
            this.getNavigation().startMovingTo(this.getX() + escapeDirection.x * 3,
                    this.getY(),
                    this.getZ() + escapeDirection.z * 3,
                    1.5);
        }
    }

    public void doWakeUp() {
        restTime = 0;
        isResting = false;
        this.goalSelector.enableControl(Goal.Control.MOVE);
        this.goalSelector.enableControl(Goal.Control.LOOK);
        this.goalSelector.enableControl(Goal.Control.TARGET);
        this.setPose(EntityPose.STANDING);
        this.setMovementSpeed(1.0F);
        this.goalSelector.getGoals().forEach(goal -> goal.start());
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean damaged = super.damage(world, source, amount);
        if (damaged && this.getPose() == EntityPose.SLEEPING && source.getAttacker() instanceof LivingEntity attacker) {

            if (attacker.isInCreativeMode() || attacker.isSpectator()) return damaged;
            if (attacker instanceof PlayerEntity && world.getDifficulty() == Difficulty.PEACEFUL) 
                return damaged;
            if (squaredDistanceTo(attacker) > 900.0D) return damaged;

            doWakeUp();
            if (!prideMembers.isEmpty()) {
                for (Lion lion : prideMembers) {
                    lion.wakeUp();
                    lion.setTarget(attacker);
                }
            }
            this.setTarget(attacker); // Attack the attacker
        }
        return damaged;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return false;

        if (livingTarget.isDead()) return false;
        if (livingTarget.isInCreativeMode() || livingTarget.isSpectator()) return false;

        boolean success = super.tryAttack(world, livingTarget);
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
    public Lion createChild(ServerWorld world, PassiveEntity mate) {
        Lion cub = ModEntities.LION.create(world, null, this.getBlockPos(), SpawnReason.BREEDING, false, false);

        if (cub != null) {
            cub.setBaby(true);
        }

        return cub;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BEEF);
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F; // 50% size for babies
    }

    private int restTime = 0; // Counts how many ticks the lion has been resting
    private boolean isResting = false;

    @Override
    public void tick() {
        super.tick();

        if (!isResting) return;

        restTime--;

        if (restTime <= 0) {
            isResting = false; // Stop resting after the timer runs out
            doWakeUp(); // Switch back to standing pose
            System.out.println("Lion is standing up!");
        }
    }

    public void rest() {
        if (getSleepingSpot() == null) findSleepingSpot();
        BlockPos sleepingSpot = getSleepingSpot();
        this.getNavigation().startMovingTo(sleepingSpot.getX(), sleepingSpot.getY(), sleepingSpot.getZ(), 1.0);
        if (this.squaredDistanceTo(sleepingSpot.getX(), sleepingSpot.getY(), sleepingSpot.getZ()) <= 25) {
            this.goalSelector.disableControl(Goal.Control.MOVE);
            this.goalSelector.disableControl(Goal.Control.LOOK);
            this.goalSelector.disableControl(Goal.Control.TARGET);

            this.getNavigation().stop();
            this.setVelocity(0, 0, 0);
            this.getNavigation().stop();
            this.setPose(EntityPose.CROUCHING);
            this.setMovementSpeed(0.0F);
        }
        restTime = this.getWorld().isDay() ? 800 : 60;
        isResting = true;
    }

    public boolean isResting() {
        return isResting;
    }

    public Boolean isMale() {
        return isMale;
    }

    private BlockPos sleepingSpot = null;

    public BlockPos getSleepingSpot() {
        return sleepingSpot;
    }

    public void setSleepingSpot(BlockPos pos) {
        this.sleepingSpot = pos;
    }

    private boolean isShaded(@NotNull BlockPos pos, @NotNull World world) {
        return !world.isSkyVisible(pos);
    }

    public BlockPos findSleepingSpot() {
        if (this.sleepingSpot != null &&
            this.sleepingSpot.isWithinDistance(this.getBlockPos(), 20) &&
            !isShaded(this.sleepingSpot, this.getWorld())
        ) {
            return this.sleepingSpot; // Return the remembered spot
        }

        BlockPos currentPos = this.getBlockPos();
        World world = this.getWorld();

        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                BlockPos checkPos = currentPos.add(x, 0, z);
                if (!isShaded(checkPos, world)) continue;

                this.setSleepingSpot(checkPos); // Store the spot for later use
                return checkPos;
            }
        }

        // No good spot found, sleep at the current position
        this.setSleepingSpot(currentPos);
        return currentPos;
    }
}