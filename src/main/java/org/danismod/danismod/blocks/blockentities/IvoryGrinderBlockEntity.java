package org.danismod.danismod.blocks.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.danismod.danismod.items.ModItems;
import org.danismod.danismod.screenhandlers.GrinderScreenHandler;
import org.danismod.others.GrinderFuelRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IvoryGrinderBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory  {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final int OUTPUT_AMOUNT = 3;

    public IvoryGrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IVORY_GRINDER_BLOCK_ENTITY_TYPE, pos, state);
    }
    // Placeholder comment: Resource box offset is 25px
    private int progress = 0;
    private int fuelUsesLeft = 0;
    private static final int FUEL_SLOT = 2;
    private static final int MAX_FUEL_USES = 70;
    private static final int MAX_PROGRESS = 50;

    public int getRemainingFuel() { return fuelUsesLeft; }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 2 -> fuelUsesLeft;  // Track remaining fuel usages
                case 0 -> progress;      // Track progress
                case 1 -> MAX_PROGRESS;  // Optional: Max progress for UI scaling
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 1 -> fuelUsesLeft = value;
                case 0 -> progress = value;
            }
        }
        @Override
        public int size() {
            return 3;
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
        return side == Direction.DOWN ? new int[]{1} : new int[]{0, 2};
        // DOWN can extract from slot 1 (Output)
        // Other sides can insert into slot 0 (Input)
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if (stack.isOf(ModItems.ELEPHANT_TUSK))
            return slot == 0; // Only allow inserting into input slot
        else if (stack.isOf(Items.BLAZE_POWDER))
            return slot == 1;
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1; // Only allow extracting from output slot
    }


    public static void tick(World world, BlockPos pos, BlockState state, IvoryGrinderBlockEntity be) {
        if (world.isClient) return;

        boolean dirty = false;

        // If no fuel left, try consuming fuel from the fuel slot
        if (be.fuelUsesLeft <= 0) {
            ItemStack fuelStack = be.inventory.get(FUEL_SLOT);
            if (!GrinderFuelRegistry.isFuel(fuelStack)) return;
            int fuelUses = GrinderFuelRegistry.getFuelUses(fuelStack);
            if (!fuelStack.isEmpty()) {
                be.fuelUsesLeft = MathHelper.clamp(fuelUses, 0, MAX_FUEL_USES); // Load fuel uses
                fuelStack.decrement(1);
                dirty = true;
            }
        }

        // If grinding is not possible, reset progress
        if (!be.canGrind() || be.fuelUsesLeft <= 0) {
            be.progress = 0;
            return;
        }

        // Process grinding
        be.progress++;
        if (be.progress >= MAX_PROGRESS) {
            be.grindItem();
            be.progress = 0;
            be.fuelUsesLeft--; // Decrease fuel uses only when an item is processed
            dirty = true;
        }

        // Dirty as heck!
        if (dirty) {
            markDirty(world, pos, state);
        }
    }

    private boolean canGrind() {
        return !inventory.get(0).isEmpty() &&
            inventory.get(1).getCount() <= 61 &&
            ((inventory.get(1).isEmpty() || inventory.get(1).isOf(ModItems.IVORY))) &&
            isValidGrindableItem(inventory.get(0));
    }

    private boolean isValidGrindableItem(@NotNull ItemStack stack) {
        return stack.isOf(ModItems.ELEPHANT_TUSK);
    }

    private void grindItem() {
        ItemStack input = inventory.get(0);
        ItemStack output = new ItemStack(ModItems.IVORY, OUTPUT_AMOUNT); // Example output

        int outputSlot = 1;

        if (inventory.get(outputSlot).isOf(ModItems.IVORY)) {
            ItemStack existingStack = inventory.get(outputSlot);

            if (existingStack.isOf(output.getItem()) && existingStack.getCount() + output.getCount() <= 64) {
                existingStack.increment(output.getCount());
            } else {
                return;
            }
        } else if (inventory.get(outputSlot).isEmpty()) {
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
        return Text.translatable("container.danismod.ivory_grinder");
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("FuelUsesLeft", fuelUsesLeft);
        nbt.putInt("Progress", progress);
        Inventories.writeNbt(nbt, this.inventory, registries);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registries);
        this.fuelUsesLeft = nbt.getInt("FuelUsesLeft").get();
        this.progress = nbt.getInt("Progress").get();
    }
}
