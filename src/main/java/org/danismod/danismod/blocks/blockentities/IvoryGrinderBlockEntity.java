package org.danismod.danismod.blocks.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.danismod.danismod.items.ModItems;
import org.danismod.danismod.screenhandlers.GrinderScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IvoryGrinderBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory  {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final int OUTPUT_AMOUNT = 3;

    public IvoryGrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IVORY_GRINDER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private int progress = 0;
    private static final int MAX_PROGRESS = 200;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return index == 0 ? progress : MAX_PROGRESS;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int size() {
            return 2;
        }
    };

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.getWorld().getBlockEntity(pos) == this &&
                player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN ? new int[]{1} : new int[]{0};
        // DOWN can extract from slot 1 (Output)
        // Other sides can insert into slot 0 (Input)
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 0; // Only allow inserting into input slot
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1; // Only allow extracting from output slot
    }


    public static void tick(World world, BlockPos pos, BlockState state, IvoryGrinderBlockEntity be) {
        if (world.isClient) return;
        if (!be.canGrind()) return;

        be.progress++;
        if (be.progress >= MAX_PROGRESS) {
            be.grindItem();
            be.progress = 0;
        }
        markDirty(world, pos, state);
    }

    private boolean canGrind() {
        return !inventory.get(0).isEmpty() &&
            inventory.get(1).getCount() < inventory.get(1).getMaxCount() + OUTPUT_AMOUNT &&
            isValidGrindableItem(inventory.get(0));
    }

    private boolean isValidGrindableItem(@NotNull ItemStack stack) {
        return stack.isOf(ModItems.ELEPHANT_TUSK);
    }

    private void grindItem() {
        ItemStack input = inventory.get(0);
        ItemStack output = new ItemStack(ModItems.IVORY, 3); // Example output

        int outputSlot = 1;

        if (!inventory.get(outputSlot).isEmpty()) {
            ItemStack existingStack = inventory.get(outputSlot);

            if (existingStack.isOf(output.getItem()) && existingStack.getCount() + output.getCount() <= existingStack.getMaxCount() + OUTPUT_AMOUNT) {
                existingStack.increment(output.getCount());
            } else {
                return;
            }
        } else {
            // If empty, place new stack
            inventory.set(outputSlot, output.copy());
        }

        input.decrement(1);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GrinderScreenHandler(syncId, inv, this, propertyDelegate);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("danismod.container.ivory_grinder");
    }

    @Override
    public void clear() {

    }
}
