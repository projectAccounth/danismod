package org.danismod.danismod.entity.mob_states_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class GroupManager extends PersistentState {
    private final Map<Integer, List<UUID>> groupMembers = new HashMap<>();

    public static final Type<GroupManager> TYPE = new Type<>(
        GroupManager::new,
        GroupManager::fromNbt,
        DataFixTypes.LEVEL
    );

    public static GroupManager get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
            TYPE,
            "lion_groups"
        );
    }

    public static GroupManager fromNbt(NbtCompound nbt, WrapperLookup lookup) {
        GroupManager manager = new GroupManager();
        NbtList groupList = nbt.getList("groups", NbtElement.COMPOUND_TYPE);

        for (NbtElement element : groupList) {
            NbtCompound compound = (NbtCompound) element;
            int groupId = compound.getInt("Id");
            NbtList members = compound.getList("Members", NbtElement.INT_ARRAY_TYPE);

            List<UUID> memberUuids = new ArrayList<>();
            for (NbtElement uuidElement : members) {
                memberUuids.add(NbtHelper.toUuid(uuidElement));
            }

            manager.groupMembers.put(groupId, memberUuids);
        }

        return manager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, WrapperLookup lookup) {
        NbtList groupList = new NbtList();
        for (Map.Entry<Integer, List<UUID>> entry : groupMembers.entrySet()) {
            NbtCompound groupCompound = new NbtCompound();
            groupCompound.putInt("Id", entry.getKey());

            NbtList uuidList = new NbtList();
            for (UUID uuid : entry.getValue()) {
                uuidList.add(NbtHelper.fromUuid(uuid));
            }

            groupCompound.put("Members", uuidList);
            groupList.add(groupCompound);
        }

        nbt.put("groups", groupList);
        return nbt;
    }

    public void addEntityToGroup(int groupId, UUID entityId) {
        groupMembers.computeIfAbsent(groupId, k -> new ArrayList<>()).add(entityId);
        markDirty();
    }

    public List<UUID> getGroupMembers(int groupId) {
        return groupMembers.getOrDefault(groupId, List.of());
    }
}
