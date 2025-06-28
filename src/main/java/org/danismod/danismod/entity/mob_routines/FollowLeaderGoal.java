package org.danismod.danismod.entity.mob_routines;

import org.danismod.danismod.entity.mobs.HasLeaderEntity;

import net.minecraft.entity.ai.goal.Goal;

public class FollowLeaderGoal<T extends HasLeaderEntity<T>> extends Goal {
    private final T entity;
    private T leader;
    private final double speed;
    private final double minDist;

    public FollowLeaderGoal(T entity, double speed, double minDist) {
        this.entity = entity;
        this.speed = speed;
        this.minDist = minDist;
    }

    @Override
    public boolean canStart() {
        leader = entity.getLeader();
        return leader != null && entity.squaredDistanceTo(leader) > minDist * minDist && entity.age % 20 == 0;
    }

    @Override
    public void start() {
        if (leader != null) {
            entity.getNavigation().startMovingTo(leader, this.speed);
        }
    }

    @Override
    public void stop() {
        entity.getNavigation().stop();
        super.stop();
    }
}
