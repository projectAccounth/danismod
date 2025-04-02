package org.danismod.danismod.screenhandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import org.danismod.danismod.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class GrinderScreenHandler extends ScreenHandler {
    private final PropertyDelegate propertyDelegate;
    private final Inventory inventory;

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(2));
    }

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.GRINDER_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        // Slots: 0 = Input, 1 = Output, -1 = Fuel input (maybe later)
        this.addSlot(new Slot(inventory, 0, 56, 35)); // Input slot
        this.addSlot(new Slot(inventory, 1, 116, 35)); // Output slot

        // Player Inventory Slots (9 Hotbar + 27 Inventory)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        // Track properties (progress bar)
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    private boolean isValidGrindableItem(@NotNull ItemStack stack) {
        return stack.isOf(ModItems.ELEPHANT_TUSK);
    }

    public float getGrindingProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        return maxProgress == 0 ? 0 : (float) progress / maxProgress;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack newStack = slot.getStack();
        ItemStack originalStack = newStack.copy();

        final int INVENTORY_START = 2; // Start of the player inventory slots
        final int HOTBAR_END = INVENTORY_START + 27;
        final int INVENTORY_END = HOTBAR_END + 9;

        if (index == 1) { // If it's the output slot
            // Move output item to player inventory
            if (!this.insertItem(newStack, INVENTORY_START, INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickTransfer(newStack, originalStack);
        } else if (index >= INVENTORY_START) { // If it's from player inventory
            // Check if item is valid for grinding (implement your check)
            if (isValidGrindableItem(newStack)) {
                if (!this.insertItem(newStack, 0, 1, false)) { // Move to input slot
                    return ItemStack.EMPTY;
                }
            } else { // Move between player inventory and hotbar
                if (index < HOTBAR_END) {
                    if (!this.insertItem(newStack, HOTBAR_END, INVENTORY_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(newStack, INVENTORY_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else { // If it's from the grinder's input slot
            if (!this.insertItem(newStack, INVENTORY_START, INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (newStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return originalStack;
    }
}

