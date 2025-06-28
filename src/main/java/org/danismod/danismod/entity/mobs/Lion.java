package org.danismod.danismod.entity.mobs;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;

import org.danismod.danismod.entity.ModEntities;
import org.danismod.danismod.entity.mob_routines.FollowLeaderGoal;
import org.danismod.danismod.sounds.ModSoundEvents;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Lion extends HasLeaderEntity<Lion> {

    private static final TrackedData<Boolean> IS_MALE = DataTracker.registerData(Lion.class, TrackedDataHandlerRegistry.BOOLEAN);

    private BlockPos sleepingSpot = null;
    private boolean  isResting = false;
    private boolean  isMovingToRestPlace = false;
    private int      restTime = 0;
    private int      findCooldown = 0;
    public boolean   isMoving;

    private long               nextRoutineTime = 0;
    private static final int   STRONG_PREY_HEALTH = 12;
    private static final int   DAY_PREY_RADIUS = 8;
    private static final int   NIGHT_PREY_RADIUS = 20;
    private static final float DAY_ROAM_CHANCE = 0.2F;
    private static final float NIGHT_ROAM_CHANCE = 0.4F;

    public Lion(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_MALE, true);
    }

    public boolean isMale() {
        return this.dataTracker.get(IS_MALE);
    }

    public void setMale(boolean male) {
        this.dataTracker.set(IS_MALE, male);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        getLeaderId().ifPresent(uuid -> nbt.putUuid("LeaderId", uuid));
        nbt.putBoolean("Gender", isMale());
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (getWorld().isClient) return;

        if (nbt.contains("Gender")) {
            setMale(nbt.getBoolean("Gender"));
        }

        if (nbt.contains("LeaderId")) {
            UUID uuid = nbt.getUuid("LeaderId");
            Entity entity = ((ServerWorld) getWorld()).getEntity(uuid);
            if (entity instanceof Lion leader && leader.isAlive()) {
                this.setLeader(leader);
            } else {
                setLeaderId(Optional.empty());
                setLeader(null);
            }
        }
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 30.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.FOLLOW_RANGE, 20.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.2)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        // this.goalSelector.add(1, new LionRoutineGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.1, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, false, this::isValidPrey));
        this.goalSelector.add(3, new FollowLeaderGoal<>(this, 1.1, 5.0));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.15));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0)); // <-- Add this line
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BEEF);
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
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);

        if (getRandom().nextFloat() < 0.2F) {
            setBaby(true);
        }

        setMale(getRandom().nextFloat() < 0.5F);

        var nearby = world.toServerWorld().getEntitiesByClass(
                Lion.class, getBoundingBox().expand(70), e -> e != this
        );

        if (nearby.isEmpty()) {
            setLeader(this);
        } else {
            Lion possibleLeader = nearby.get(0).getLeader();
            if (possibleLeader == null || !possibleLeader.isAlive()) {
                setLeader(findNewLeader(this));
            } else {
                possibleLeader.addGroupMember(this);
            }
        }

        return entityData;
    }

    @Override
    public void tick() {
        if (getWorld().isClient) {
            super.tick();
            return;
        }
        // Sync leader by UUID
        if (!hasLeader() && getLeaderId().isPresent()) {
            Entity entity = ((ServerWorld) getWorld()).getEntity(getLeaderId().get());
            if (entity instanceof Lion lion && lion.isAlive()) {
                setLeader(lion);
            } else {
                setLeader(null);
                setLeaderId(Optional.empty());
            }
        }

        // Try to form group if no members are present
        if (getGroupMembers().isEmpty()) {
            setLeader(findNewLeader(this));
            findCooldown = 20;
        }

        if (findCooldown > 0) {
            findCooldown--;
        }

        isMoving = !getNavigation().isIdle();

        if (restTime > 0) {
            restTime--;
        }

        if (restTime <= 0 && isResting) {
            isResting = false;
            doWakeUp();
        }

        if (isMovingToRestPlace) {
            findSleepingSpot();

            if (squaredDistanceTo(sleepingSpot.getX(), sleepingSpot.getY(), sleepingSpot.getZ()) <= 16) {
                goalSelector.disableControl(Goal.Control.MOVE);
                goalSelector.disableControl(Goal.Control.LOOK);
                goalSelector.disableControl(Goal.Control.TARGET);

                getNavigation().stop();
                setVelocity(0, 0, 0);
                setPose(EntityPose.CROUCHING);
                setMovementSpeed(0.0F);
                isMovingToRestPlace = false;
                restTime = getWorld().isDay() ? 800 : 60;
                isResting = true;
            }
        }

        if (getTarget() == null && !isResting()) {
            long time = getWorld().getTime();
            if (time >= nextRoutineTime) {
                nextRoutineTime = time + 60 + getRandom().nextBetween(20, 80);

                if (getWorld().isDay()) {
                    rest();
                } else {
                    List<MobEntity> prey = findNearbyPrey();
                    if (!prey.isEmpty() && !isBaby()) {
                        maybeStartHunting(prey);
                    } else if (getRandom().nextFloat() < getRoamChance()) {
                        startRoaming();
                    }
                }
            }
        }
        
        super.tick();
    }


    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean result = super.damage(world, source, amount);

        if (result && getPose() == EntityPose.SLEEPING && source.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.isInCreativeMode() || attacker.isSpectator()) return result;
            if (attacker instanceof PlayerEntity && world.getDifficulty() == Difficulty.PEACEFUL) return result;
            if (squaredDistanceTo(attacker) > 900.0D) return result;

            doWakeUp();
            for (Lion member : getGroupMembers()) {
                member.wakeUp();
                member.setTarget(attacker);
            }
            setTarget(attacker);
        }

        return result;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) return false;
        if (livingTarget.isDead() || livingTarget.isInCreativeMode() || livingTarget.isSpectator()) return false;

        boolean success = super.tryAttack(world, livingTarget);
        if (success) {
            target.damage(world, getDamageSources().mobAttack(this), (float) getAttributeValue(EntityAttributes.ATTACK_DAMAGE));
            playSound(SoundEvents.ITEM_WOLF_ARMOR_CRACK);
        }
        return success;
    }

    // ----- Resting -----

    public void rest() {
        if (isMovingToRestPlace) return;
        if (sleepingSpot == null) findSleepingSpot();
        BlockPos target = getSleepingSpot();

        getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), 1.0);
        isMovingToRestPlace = true;
    }

    public void doWakeUp() {
        restTime = 0;
        isResting = false;
        goalSelector.enableControl(Goal.Control.MOVE);
        goalSelector.enableControl(Goal.Control.LOOK);
        goalSelector.enableControl(Goal.Control.TARGET);
        setPose(EntityPose.STANDING);
        setMovementSpeed(1.0F);
        isMovingToRestPlace = false;
    }

    public boolean isResting() {
        return isResting;
    }

    public BlockPos getSleepingSpot() {
        return sleepingSpot;
    }

    public void setSleepingSpot(BlockPos pos) {
        this.sleepingSpot = pos;
    }

    public BlockPos findSleepingSpot() {
        if (sleepingSpot != null &&
            sleepingSpot.isWithinDistance(getBlockPos(), 20) &&
            isShaded(sleepingSpot, getWorld())) {
            return sleepingSpot;
        }

        World world = getWorld();
        for (int i = 0; i < 5; i++) {
            Vec3d pos = NoPenaltyTargeting.find(this, 15, 5);
            if (pos == null) continue;
            BlockPos candidate = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
            if (!isShaded(candidate, world)) continue;

            sleepingSpot = candidate;
            return candidate;
        }

        sleepingSpot = getBlockPos();
        return sleepingSpot;
    }

    private boolean isShaded(BlockPos pos, World world) {
        return !world.isSkyVisible(pos) && world.isAir(pos);
    }

    private void maybeStartHunting(List<MobEntity> preyList) {
        MobEntity target = preyList.stream()
                .filter(e -> canSee(e) && squaredDistanceTo(e) < 256)
                .min(Comparator.comparingDouble(this::squaredDistanceTo))
                .orElse(null);

        if (target == null) return;

        boolean isLeader = this == getLeader() || !hasLeader();

        if (isLeader) {
            int awakeMembers = (int) getGroupMembers().stream()
                    .filter(member -> !member.isResting() && member.isAlive())
                    .count();

            if (isStrongPrey(target)) {
                if (awakeMembers >= 2) {
                    roar();
                    for (Lion member : getGroupMembers()) {
                        if (!member.isResting()) {
                            member.doWakeUp();
                            member.setTarget(target);
                        }
                    }
                    setTarget(target);
                }
            } else {
                setTarget(target);
            }
        } else {
            LivingEntity leaderTarget = getLeader().getTarget();
            if (leaderTarget != null && !leaderTarget.isInCreativeMode() && !leaderTarget.isSpectator()) {
                setTarget(leaderTarget);
            }
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (isResting()) {
            // complete idle
            setVelocity(Vec3d.ZERO);
            return;
        }

        super.travel(movementInput);
    }

    private void startRoaming() {
        doWakeUp();
        Vec3d target = NoPenaltyTargeting.find(this, 10, 7);

        if (target != null) {
            getNavigation().startMovingTo(target.x, target.y, target.z, 1.2);
        } else {
            Vec3d fallback = getRotationVec(1.0F).normalize().multiply(4);
            Vec3d pos = getPos().add(fallback);
            getNavigation().startMovingTo(pos.x, pos.y, pos.z, 1.0);
        }
    }

    private void roar() {
        getWorld().playSound(null, getBlockPos(), ModSoundEvents.ENTITY_LION_ROAR, SoundCategory.HOSTILE, 1.5F, 1.0F);
        getNavigation().stop();
        isMoving = false;
    }

    private List<MobEntity> findNearbyPrey() {
        int radius = getWorld().isDay() ? DAY_PREY_RADIUS : NIGHT_PREY_RADIUS;
        return getWorld().getEntitiesByClass(MobEntity.class, getBoundingBox().expand(radius, 8, radius), this::isValidPrey);
    }

    private boolean isValidPrey(LivingEntity entity) {
        return !(entity instanceof Lion || entity instanceof FishEntity || entity instanceof SquidEntity || entity.isTouchingWater()) &&
            (isWeakPrey(entity) || isStrongPrey(entity));
    }

    private boolean isValidPrey(LivingEntity entity, ServerWorld world) {
        return isValidPrey(entity);
    }

    private boolean isWeakPrey(LivingEntity entity) {
        return entity.getHealth() <= STRONG_PREY_HEALTH;
    }

    private boolean isStrongPrey(LivingEntity entity) {
        return entity.getHealth() > STRONG_PREY_HEALTH;
    }

    private float getRoamChance() {
        return getWorld().isDay() ? DAY_ROAM_CHANCE : NIGHT_ROAM_CHANCE;
    }

}
