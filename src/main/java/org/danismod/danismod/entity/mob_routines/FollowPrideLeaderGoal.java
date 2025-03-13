package org.danismod.danismod.entity.mob_routines;

import net.minecraft.entity.ai.goal.Goal;
import org.danismod.danismod.entity.Lion;

public class FollowPrideLeaderGoal extends Goal {
    private final Lion lion;
    private Lion leader;
    private final double speed;

    public FollowPrideLeaderGoal(Lion lion, double speed) {
        this.lion = lion;
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        leader = lion.getLeader();
        return leader != null && lion.squaredDistanceTo(leader) > 5.0;
    }

    @Override
    public void start() {
        if (leader != null) {
            lion.getNavigation().startMovingTo(leader, this.speed);
        }
    }
}
