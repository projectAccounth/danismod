package org.danismod.danismod.client.guiscreens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.danismod.danismod.screenhandlers.GrinderScreenHandler;

@Environment(EnvType.CLIENT)
public class GrinderScreen extends HandledScreen<GrinderScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of("danismod", "textures/gui/grinder.png");
    private static final Identifier PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress");
    private static final Identifier FUEL_TEXTURE = Identifier.ofVanilla("container/brewing_stand/fuel_length");

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        ctx.drawTexture(RenderLayer::getGuiTextured, TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);

        // Draw the progress bar
        float progress = handler.getGrindingProgress();
        int progressWidth = MathHelper.ceil(progress * 24); // Scale from 0-24 pixels

        ctx.drawGuiTexture(RenderLayer::getGuiTextured, PROGRESS_TEXTURE, 24, 16, 0, 0, this.x + 79, this.y + 34, progressWidth, 16);

        float remainingFuel = handler.getRemainingFuel();
        int length = MathHelper.ceil(remainingFuel * 18);
        if (length > 0) {
            ctx.drawGuiTexture(RenderLayer::getGuiTextured, FUEL_TEXTURE, 18, 4, 0, 0, this.x + 30, this.y + 26, length, 4);
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        drawMouseoverTooltip(ctx, mouseX, mouseY);
    }
}

