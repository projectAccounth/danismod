package org.danismod.danismod.entity.mob_routines;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import org.danismod.danismod.entity.Lion;
import org.danismod.danismod.sounds.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class LionRoutineGoal extends Goal {
    private final MobEntity lion;
    // private int cooldown = 0;

    public LionRoutineGoal(MobEntity lion) {
        this.lion = lion;
    }

    @Override
    public boolean canStart() {
        return lion.getRandom().nextFloat() <= 0.8F; // 80%
    }

    private float getRoamChance() {
        return lion.getWorld().isDay() ? 0.3F : 0.9F;
    }

    private int getTargetBoundingBox() {
        return lion.getWorld().isDay() ? 8 : 20;
    }

    // \ge
    private float getHuntingChanceAtDay() { return lion.getWorld().isDay() ? 0.15F : 0.0F; }

    @Override
    public void start() {
        if (!(lion instanceof Lion lionEntity)) return;
        if (lionEntity.isResting()) return;

        List<MobEntity> prey = this.findNearbyPreys(); // Find prey before making a decision
        lionEntity.doWakeUp();

        if (!prey.isEmpty() && !lionEntity.isBaby()) {
            if (lionEntity.getWorld().isDay() && lionEntity.getRandom().nextFloat() > this.getHuntingChanceAtDay())
                return;
            if (lionEntity.isBaby()) return;
            hunt(prey); // Hunt if prey is found
            System.out.println("Found prey! Hunting...");
        } else if (lionEntity.getRandom().nextFloat() < getRoamChance()) {
            roam(); // Roam if no prey is nearby
            System.out.println("No prey nearby. Roaming...");
        } else {
            rest(); // Rest occasionally when no prey is around
            System.out.println("No prey nearby. Resting...");
        }
    }
    private void roam() {
        if (!(lion instanceof Lion lionEntity)) return;
        lionEntity.stopResting();
        BlockPos targetPos = lionEntity.getBlockPos().add(
                lionEntity.getRandom().nextInt(15) - 5,
                0,
                lionEntity.getRandom().nextInt(15) - 5
        );
        lionEntity.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
    }

    private void rest() {
        if (!(lion instanceof Lion lionEntity)) return;
        lionEntity.rest();
    }

    private void roar() {
        lion.getWorld().playSound(
                null,
                lion.getBlockPos(),
                ModSoundEvents.ENTITY_LION_ROAR,
                SoundCategory.HOSTILE,
                1.5F,
                1.0F
        );

        // Stop movement briefly to add realism
        lion.getNavigation().stop();

        // Schedule movement after roaring
        Objects.requireNonNull(lion.getWorld().getServer()).execute(() -> {
            if (lion.getTarget() != null) {
                lion.getNavigation().startMovingTo(lion.getTarget(), 1.2);
                System.out.println("Going to target!");
            }
        });
    }

    private List<MobEntity> findNearbyPreys() {
        return lion.getWorld().getEntitiesByClass(
                MobEntity.class,
                lion.getBoundingBox().expand(getTargetBoundingBox(), 8, getTargetBoundingBox()),
                this::isValidPrey // Only select valid prey
        );
    }

    private void hunt(List<MobEntity> nearbyPrey) {
        if (!(lion instanceof Lion lionEntity)) return;
        lionEntity.stopResting();

        if (!lionEntity.hasLeader() || lionEntity == lionEntity.getLeader()) {
            if (!nearbyPrey.isEmpty()) {
                LivingEntity target = nearbyPrey.get(0); // Select first valid prey

                roar(); // Roar before hunting

                // Check if pack hunting is needed
                boolean strongPrey = isStrongPrey(target);
                lionEntity.setTarget(target);

                if (strongPrey) {
                    // never going there
                    if (lionEntity.getLeader() == null) {
                        // lionEntity.setTarget(target);
                        // lionEntity.getNavigation().startMovingTo(target, 1.5);
                        // System.out.println("Attacking strong prey individually!");
                        return;
                    }
                    // Get pride members to join the hunt
                    if (lionEntity.getLeader().prideMembers.size() < 2) {
                        for (Lion member : lionEntity.getLeader().prideMembers) {
                            member.setTarget(null);
                            System.out.println("Ignoring target, too strong. Pride size: " + lionEntity.getLeader().prideMembers.size());
                        }
                        return;
                    }
                    for (Lion member : lionEntity.getLeader().prideMembers) {
                        member.wakeUp();
                        member.setTarget(target);
                        System.out.println("Set strong target for the pride!");
                    }
                }

                lionEntity.getNavigation().startMovingTo(target, 1.5);
            }
        } else {
            Lion leader = lionEntity.getLeader();
            if (leader == null) return;
            if (leader.getTarget() != null) {
                lionEntity.setTarget(leader.getTarget());
                System.out.println("Going to attack a weak prey!");
                lionEntity.getNavigation().startMovingTo(leader.getTarget(), 1.5);
            }
        }
    }

    private boolean isWeakPrey(@NotNull LivingEntity entity) {
        return entity.getHealth() <= 12;
    }

    private boolean isStrongPrey(@NotNull LivingEntity entity) {
        return entity.getHealth() > 12;
    }

    private boolean isValidPrey(LivingEntity entity) {
        if (entity instanceof Lion ||
            entity instanceof FishEntity ||
            entity instanceof SquidEntity ||
            entity.isTouchingWater()
        )
            return false;
        return (isWeakPrey(entity) || isStrongPrey(entity));
    }
}
