package org.danismod.danismod.entity.mob_routines;

import java.util.List;

import net.minecraft.entity.mob.MobEntity;

public interface HasLeaderEntity<T extends MobEntity> {
    boolean hasLeader();
    T getLeader();
    @SuppressWarnings("unchecked")
    default List<T> getNearbyEntities(T entity) {
        return (List<T>) entity.getWorld().getEntitiesByClass(
                entity.getClass(),
                entity.getBoundingBox().expand(40, 10, 40),
                e -> e != entity
        );
    }
}
