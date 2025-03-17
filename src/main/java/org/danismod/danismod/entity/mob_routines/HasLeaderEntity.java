package org.danismod.danismod.entity.mob_routines;

import net.minecraft.entity.mob.MobEntity;

public interface HasLeaderEntity<T extends MobEntity> {
    boolean hasLeader();
    T getLeader();
}
