package dev.cotton.gui;

import dev.cotton.core.CottonVisuals;
import dev.cotton.core.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ClickGui extends Screen {

    private static final int PANEL_X = 20;
    private static final int PANEL_Y = 20;
    private static final int PANEL_W = 160;
    private static final int ROW_H = 20;
    private static final int PADDING = 6;

    private Module.Category selectedCategory = Module.Category.VISUALS;

    public ClickGui() {
        super(Text.literal("Cotton Visuals"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dark background overlay
        context.fill(0, 0, this.width, this.height, 0x88000000);

        // ── Sidebar (categories) ──
        int sideX = PANEL_X;
        int sideY = PANEL_Y;
        int sideW = 120;

        // Sidebar background
        context.fill(sideX, sideY, sideX + sideW, sideY + 180, 0xCC0D0B14);

        // Header - Cotton Visuals title
        context.fill(sideX, sideY, sideX + sideW, sideY + 28, 0xDD0A0814);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("✦ Cotton"),
                sideX + 8, sideY + 5, 0xFFD4BBFF);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("  Visuals"),
                sideX + 8, sideY + 14, 0xFF9988BB);

        // Category buttons
        Module.Category[] cats = Module.Category.values();
        for (int i = 0; i < cats.length; i++) {
            Module.Category cat = cats[i];
            int btnY = sideY + 32 + i * 22;
            boolean active = cat == selectedCategory;

            // Button background
            if (active) {
                context.fill(sideX + 4, btnY, sideX + sideW - 4, btnY + 18, 0xAA6644CC);
                context.fill(sideX + 4, btnY, sideX + 6, btnY + 18, 0xFF9966FF); // accent left border
            } else {
                context.fill(sideX + 4, btnY, sideX + sideW - 4, btnY + 18, 0x55221133);
            }

            context.drawTextWithShadow(this.textRenderer,
                    Text.literal(cat.getDisplayName()),
                    sideX + 10, btnY + 5,
                    active ? 0xFFFFFFFF : 0xFF8877AA);
        }

        // ── Module list panel ──
        int modX = sideX + sideW + 6;
        int modY = PANEL_Y;
        int modW = 280;

        // Panel background
        context.fill(modX, modY, modX + modW, modY + 280, 0xCC0D0B14);

        // Panel title
        context.fill(modX, modY, modX + modW, modY + 20, 0xDD0A0814);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal(selectedCategory.getDisplayName()),
                modX + 8, modY + 6, 0xFFD4BBFF);

        // Draw modules in 2-column grid
        List<Module> mods = CottonVisuals.moduleManager.getByCategory(selectedCategory);
        int colW = (modW - 12) / 2;
        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int col = i % 2;
            int row = i / 2;
            int mx = modX + 4 + col * (colW + 4);
            int my = modY + 24 + row * 36;

            boolean on = mod.isEnabled();
            boolean hovered = mouseX >= mx && mouseX <= mx + colW && mouseY >= my && mouseY <= my + 32;

            // Card background
            int bgColor = on ? 0xAA1A0A2E : (hovered ? 0x88151525 : 0x66100818);
            context.fill(mx, my, mx + colW, my + 32, bgColor);

            // Left accent bar when on
            if (on) {
                context.fill(mx, my, mx + 2, my + 32, 0xFF9966FF);
            }

            // Toggle indicator (right side)
            int toggleX = mx + colW - 22;
            int toggleY = my + 10;
            context.fill(toggleX, toggleY, toggleX + 18, toggleY + 10, on ? 0x884422AA : 0x44333344);
            int dotX = on ? toggleX + 9 : toggleX + 1;
            context.fill(dotX, toggleY + 1, dotX + 8, toggleY + 9, on ? 0xFFAA88FF : 0xFF666677);

            // Module name
            context.drawTextWithShadow(this.textRenderer,
                    Text.literal(mod.getName()),
                    mx + 5, my + 5,
                    on ? 0xFFFFFFFF : 0xFFAA99BB);

            // Description
            String desc = mod.getDescription();
            if (desc.length() > 20) desc = desc.substring(0, 18) + "..";
            context.drawTextWithShadow(this.textRenderer,
                    Text.literal(desc),
                    mx + 5, my + 17,
                    0xFF665577);
        }

        // Hint at bottom
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Right Shift / Esc to close"),
                PANEL_X, this.height - 14, 0xFF443355);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check category clicks (sidebar)
        int sideX = PANEL_X;
        int sideY = PANEL_Y;
        int sideW = 120;
        Module.Category[] cats = Module.Category.values();
        for (int i = 0; i < cats.length; i++) {
            int btnY = sideY + 32 + i * 22;
            if (mouseX >= sideX + 4 && mouseX <= sideX + sideW - 4
                    && mouseY >= btnY && mouseY <= btnY + 18) {
                selectedCategory = cats[i];
                return true;
            }
        }

        // Check module clicks
        int modX = sideX + sideW + 6;
        int modY = PANEL_Y;
        int modW = 280;
        int colW = (modW - 12) / 2;
        List<Module> mods = CottonVisuals.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int col = i % 2;
            int row = i / 2;
            int mx = modX + 4 + col * (colW + 4);
            int my = modY + 24 + row * 36;
            if (mouseX >= mx && mouseX <= mx + colW && mouseY >= my && mouseY <= my + 32) {
                mods.get(i).toggle();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
