package org.danismod.danismod.entity.mobs;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class HasLeaderEntity<T extends HasLeaderEntity<T>> extends AnimalEntity {
    protected T groupLeader;
    protected final List<T> groupMembers = new ArrayList<>();
    protected static final TrackedData<String> LEADER_ID =
            DataTracker.registerData(HasLeaderEntity.class, TrackedDataHandlerRegistry.STRING);

    protected HasLeaderEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    public T getLeader() {
        if (this.groupLeader != null && this.groupLeader.isAlive()) {
            return this.groupLeader;
        }
        return null;
    }


    public boolean hasLeader() {
        return getLeader() != null && getLeader().isAlive();
    }

    @Nullable
    T findNewLeader(T thisEntity) {
        T leader = getLeader();
        if (!this.hasLeader() || leader == null || !leader.isAlive()) {
            List<T> entities = getNearbyEntities(thisEntity);

            if (!entities.isEmpty()) {
                T newLeader = entities.get(0);
                for (T entity : entities) {
                    if (entity.isBaby()) continue;
                    if (entity.age > newLeader.age) {
                        newLeader = entity;
                    } else if (entity.age == newLeader.age) {
                        // tie-breaker using UUID comp
                        if (entity.getUuid().compareTo(newLeader.getUuid()) < 0) {
                            newLeader = entity;
                        }
                    }
                }

                return newLeader;
            } else {
                return thisEntity;
            }
        }
        
        return leader;
    }

    @SuppressWarnings("unchecked")
    public List<T> getNearbyEntities(T entity) {
        return (List<T>) entity.getWorld().getEntitiesByClass(
                entity.getClass(),
                entity.getBoundingBox().expand(40, 10, 40),
                e -> e != entity
        );
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LEADER_ID, "");
    }

    public void setLeader(@Nullable T leader) {
        if (leader == null || !leader.isAlive()) {
            T newLeader = findNewLeader(this.self());
            this.groupLeader = newLeader;
            if (newLeader != null && newLeader.isAlive()) {
                setLeaderId(Optional.of(newLeader.getUuid()));
                if (!newLeader.groupMembers.contains(this.self()))
                    newLeader.groupMembers.add(this.self());
                this.groupMembers.clear();
            } else {
                setLeaderId(Optional.empty());
                this.groupLeader = null;
            }
        } else {
            this.groupLeader = leader;
            setLeaderId(Optional.of(leader.getUuid()));
            if (!leader.groupMembers.contains(this.self())) {
                leader.groupMembers.add(this.self());
                this.groupMembers.clear();
            }
        }
    }

    public void addGroupMember(T member) {
        if (!groupMembers.contains(member)) {
            groupMembers.add(member);
            member.setLeader(this.self());
        }
    }

    public List<T> getGroupMembers() {
        return groupMembers;
    }

    public Optional<UUID> getLeaderId() {
        String s = this.dataTracker.get(LEADER_ID);
        if (s == null || s.isEmpty()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    public void setLeaderId(Optional<UUID> id) {
        this.dataTracker.set(LEADER_ID, id.map(UUID::toString).orElse(""));
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    // save-load

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        getLeaderId().ifPresent(uuid -> nbt.putString("LeaderId", uuid.toString()));
        return nbt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (this.getWorld().isClient) return;
        if (!nbt.contains("LeaderId")) {
            setLeaderId(Optional.empty());
            return;
        }
        String idStr = nbt.getString("LeaderId", "");
        Optional<UUID> id = idStr.isEmpty() ? Optional.empty() : Optional.of(UUID.fromString(idStr));
        setLeaderId(id);

        id.ifPresent(uuid -> {
            T leaderEntity = null;
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                leaderEntity = (T) serverWorld.getEntity(uuid);
            }
            if (leaderEntity == null || !leaderEntity.isAlive()) {
                return;
            }
            this.setLeader(leaderEntity);
        });
    }

    @Override
    public EntityData initialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData
    ) {
        super.initialize(world, difficulty, spawnReason, entityData);

        // Find nearby entities of the same type to form a group
        @SuppressWarnings("unchecked")
        List<T> nearby = world.toServerWorld().getEntitiesByClass(
            (Class<T>) this.getClass(), this.getBoundingBox().expand(70), e -> e != this
        );

        if (nearby.isEmpty()) {
            this.setLeader(this.self());
        } else {
            T leader = nearby.get(0).getLeader();
            if (leader == null || !leader.isAlive()) {
                setLeader(findNewLeader(this.self()));
            } else {
                leader.addGroupMember(this.self());
            }
        }
        return entityData;
    }
}