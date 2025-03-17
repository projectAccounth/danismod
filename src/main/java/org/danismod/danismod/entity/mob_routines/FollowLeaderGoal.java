package org.danismod.danismod.entity.mob_routines;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

public class FollowLeaderGoal<T extends MobEntity & HasLeaderEntity<T>> extends Goal {
    private final T entity;
    private T leader;
    private final double speed;

    public FollowLeaderGoal(T entity, double speed) {
        this.entity = entity;
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        leader = entity.getLeader();
        return leader != null && entity.squaredDistanceTo(leader) > 25.0;
    }

    @Override
    public void start() {
        if (leader != null) {
            entity.getNavigation().startMovingTo(leader, this.speed);
        }
    }
}
