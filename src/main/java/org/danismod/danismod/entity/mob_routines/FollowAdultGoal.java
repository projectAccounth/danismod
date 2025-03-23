package org.danismod.danismod.entity.mob_routines;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.List;

public class FollowAdultGoal extends Goal {
    MobEntity mainEntity;
    double speed;

    public FollowAdultGoal(MobEntity entity, double speed) {
        this.mainEntity = entity;
        this.speed = speed;
    }

    private List<? extends MobEntity> getNearbyEntities() {
        return mainEntity.getWorld().getEntitiesByClass(
            this.mainEntity.getClass(),
            this.mainEntity.getBoundingBox().expand(8.0D),
            (entity) -> entity != this.mainEntity && entity.isAlive() && !this.mainEntity.isBaby()
        );
    }

    @Override
    public boolean canStart() {
        return mainEntity.getRandom().nextFloat() < 0.8 && this.mainEntity.isBaby();
    }

    @Override
    public void start() {
        super.start();
        var nearbyEntities = getNearbyEntities();
        if (nearbyEntities.size() > 0) {
            var target = nearbyEntities.get(0);
            this.mainEntity.getNavigation().startMovingTo(target, this.speed);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void tick() {
        super.tick();
    }
    
}
