package dev.cotton.gui;

import dev.cotton.core.CottonVisuals;
import dev.cotton.core.Module;
import dev.cotton.render.DrawHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static dev.cotton.render.DrawHelper.*;

public class ClickGui extends Screen {

    // ── Layout ──────────────────────────────────────────────────────────────
    private static final int GUI_W    = 590;
    private static final int GUI_H    = 545;
    private static final int SIDEBAR_W = 175;
    private static final int CARD_H   = 36;
    private static final int CARD_GAP = 5;
    private static final int COL_GAP  = 6;
    private static final int HEADER_H = 42;
    private static final int FOOTER_H = 40;
    private static final int NAV_ROW_H = 22;

    // ── State ────────────────────────────────────────────────────────────────
    private int guiX, guiY;
    private Module.Category selectedCat = Module.Category.VISUALS;

    private boolean dragging = false;
    private int dragOffX, dragOffY;

    private String searchQuery = "";
    private Module hoveredModule = null;
    private int guiStyle = 0; // 0=liquid, 1=classic, 2=custom

    public ClickGui() {
        super(Text.literal("Cotton Visuals"));
    }

    @Override
    protected void init() {
        guiX = (this.width  - GUI_W) / 2;
        guiY = (this.height - GUI_H) / 2;
    }

    @Override
    public boolean shouldPause() { return false; }

    // ── Render ───────────────────────────────────────────────────────────────
    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Dark overlay
        ctx.fill(0, 0, this.width, this.height, 0x88000000);

        int x = guiX, y = guiY;

        // Outer container
        ctx.fill(x, y, x + GUI_W, y + GUI_H, BG_DARK);
        border(ctx, x, y, GUI_W, GUI_H, BORDER_SUBTLE);

        // Sidebar
        drawSidebar(ctx, x, y, mouseX, mouseY);

        // Divider
        ctx.fill(x + SIDEBAR_W, y, x + SIDEBAR_W + 1, y + GUI_H, BORDER_SUBTLE);

        // Main panel
        int px = x + SIDEBAR_W;
        ctx.fill(px, y, x + GUI_W, y + GUI_H, BG_PANEL);
        drawMainPanel(ctx, px, y, GUI_W - SIDEBAR_W, mouseX, mouseY);

        // Tooltip
        if (hoveredModule != null) {
            drawTooltip(ctx, hoveredModule);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────
    private void drawSidebar(DrawContext ctx, int x, int y, int mx, int my) {
        int sw = SIDEBAR_W;
        ctx.fill(x, y, x + sw, y + GUI_H, BG_SIDEBAR);

        // Logo header
        ctx.fill(x, y, x + sw, y + 52, color(0x33, 0x0A, 0x08, 0x14));
        divider(ctx, x, y + 52, sw);

        drawCottonIcon(ctx, x + 10, y + 10);
        textShadow(ctx, "Cotton",     x + 48, y + 12, TEXT_ACCENT);
        text(ctx,       "Visuals v6", x + 48, y + 23, TEXT_DIM);

        // Nav
        int ny = y + 60;
        text(ctx, "VISUAL", x + 10, ny, color(0x38, 0xFF, 0xFF, 0xFF));
        ny += 11;

        ny = drawNavItem(ctx, x, ny, sw, "\u2726", "Visuals",  Module.Category.VISUALS,  mx, my);
        ny = drawNavItem(ctx, x, ny, sw, "\u25c8", "HUD",      Module.Category.HUD,      mx, my);
        ny = drawNavItem(ctx, x, ny, sw, "\u2727", "Particles",Module.Category.PARTICLES, mx, my);

        ny += 4;
        text(ctx, "WORLD", x + 10, ny, color(0x38, 0xFF, 0xFF, 0xFF));
        ny += 11;

        ny = drawNavItem(ctx, x, ny, sw, "\u25c9", "World",    Module.Category.WORLD,    mx, my);
        ny = drawNavItem(ctx, x, ny, sw, "\u266a", "Sounds",   Module.Category.SOUNDS,   mx, my);

        ny += 4;
        text(ctx, "CONFIG", x + 10, ny, color(0x38, 0xFF, 0xFF, 0xFF));
        ny += 11;

        drawNavItem(ctx, x, ny, sw, "\u2699", "Settings", Module.Category.SETTINGS, mx, my);

        // Footer
        int fy = y + GUI_H - FOOTER_H;
        divider(ctx, x, fy, sw);
        ctx.fill(x, fy, x + sw, y + GUI_H, color(0x22, 0x0A, 0x08, 0x14));
        ctx.fill(x + 10, fy + 8, x + 36, fy + 32, color(0xFF, 0x4A, 0xDE, 0x80));
        textShadow(ctx, "S", x + 19, fy + 14, 0xFFFFFFFF);
        text(ctx, "Steve",       x + 42, fy + 10, TEXT_PRIMARY);
        text(ctx, "Cotton User", x + 42, fy + 20, TEXT_DIM);
    }

    private int drawNavItem(DrawContext ctx, int x, int y, int sw,
                             String icon, String label, Module.Category cat,
                             int mx, int my) {
        boolean active  = cat == selectedCat;
        boolean hovered = mx >= x + 6 && mx <= x + sw - 6 && my >= y && my <= y + NAV_ROW_H;

        int bg = active ? BG_NAV_ACTIVE : (hovered ? color(0x12, 0xFF, 0xFF, 0xFF) : 0);
        if (bg != 0) ctx.fill(x + 6, y, x + sw - 6, y + NAV_ROW_H, bg);
        if (active)  ctx.fill(x + 6, y, x + 8, y + NAV_ROW_H, ACCENT_PURPLE);

        int tc = active ? TEXT_PRIMARY : (hovered ? TEXT_SECONDARY : TEXT_DIM);
        text(ctx, icon + "  " + label, x + 14, y + 6, tc);

        return y + NAV_ROW_H + 2;
    }

    // ── Main panel ───────────────────────────────────────────────────────────
    private void drawMainPanel(DrawContext ctx, int px, int py, int pw, int mx, int my) {
        // Header
        ctx.fill(px, py, px + pw, py + HEADER_H, color(0x22, 0x0A, 0x08, 0x14));
        divider(ctx, px, py + HEADER_H, pw);
        textShadow(ctx, selectedCat.getDisplayName(), px + 12, py + 13, TEXT_ACCENT);

        // Search
        int sx = px + pw - 130;
        ctx.fill(sx, py + 9, px + pw - 10, py + 32, color(0x12, 0xFF, 0xFF, 0xFF));
        border(ctx, sx, py + 9, 120, 23, BORDER_SUBTLE);
        text(ctx, "\u2315  " + (searchQuery.isEmpty() ? "Search..." : searchQuery),
                sx + 6, py + 15, searchQuery.isEmpty() ? TEXT_DIM : TEXT_PRIMARY);

        // Module grid
        List<Module> mods = getFilteredModules();
        hoveredModule = null;

        int colW   = (pw - 24) / 2;
        int startY = py + HEADER_H + 8;
        int maxY   = py + GUI_H - FOOTER_H - 4;

        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int col = i % 2;
            int row = i / 2;
            int cx = px + 8 + col * (colW + COL_GAP);
            int cy = startY + row * (CARD_H + CARD_GAP);

            if (cy + CARD_H > maxY) break;

            boolean hov = mx >= cx && mx <= cx + colW && my >= cy && my <= cy + CARD_H;
            if (hov) hoveredModule = mod;

            moduleCard(ctx, cx, cy, colW, CARD_H, mod.isEnabled(), mod.isPinned(), hov);
            drawModuleCard(ctx, mod, cx, cy, colW, CARD_H);
        }

        // Style bar
        drawStyleBar(ctx, px, py + GUI_H - FOOTER_H, pw);
    }

    private void drawModuleCard(DrawContext ctx, Module mod, int x, int y, int w, int h) {
        boolean on = mod.isEnabled();
        textShadow(ctx, mod.getName(),        x + 8, y + 6,  on ? TEXT_PRIMARY : TEXT_SECONDARY);
        text(ctx, truncate(mod.getDescription(), 22), x + 8, y + 18, TEXT_DIM);
        toggle(ctx, x + w - 36, y + (h - 15) / 2, on);

        int pinColor = mod.isPinned() ? ACCENT_GOLD : TEXT_HINT;
        text(ctx, "\u2605", x + w - 52, y + (h - 8) / 2, pinColor);

        String bindLabel = mod.getKeyBind() == -1 ? "n/a" : "K" + mod.getKeyBind();
        text(ctx, bindLabel, x + 8, y + h - 10, color(0x38, 0x8B, 0x5C, 0xF6));
    }

    private void drawStyleBar(DrawContext ctx, int px, int py, int pw) {
        ctx.fill(px, py, px + pw, py + FOOTER_H, color(0x22, 0x0A, 0x08, 0x14));
        divider(ctx, px, py, pw);
        text(ctx, "Style:", px + 10, py + 13, TEXT_DIM);

        String[] labels = { "Liquid", "Classic", "Custom" };
        int btnX = px + 60;
        for (int i = 0; i < labels.length; i++) {
            boolean active = guiStyle == i;
            int bg = active ? color(0x55, 0x8B, 0x5C, 0xF6) : color(0x12, 0xFF, 0xFF, 0xFF);
            ctx.fill(btnX, py + 8, btnX + 68, py + 30, bg);
            border(ctx, btnX, py + 8, 68, 22, active ? BORDER_ACTIVE : BORDER_SUBTLE);
            textCentered(ctx, labels[i], btnX + 34, py + 14, active ? TEXT_ACCENT : TEXT_SECONDARY);
            btnX += 74;
        }
    }

    // ── Tooltip ──────────────────────────────────────────────────────────────
    private void drawTooltip(DrawContext ctx, Module mod) {
        String desc = mod.getDescription();
        var tr = client.textRenderer;
        int tw = tr.getWidth(desc);
        int tx = Math.max(4, Math.min(guiX + (GUI_W - tw) / 2, this.width - tw - 8));
        int ty = guiY - 28;

        ctx.fill(tx - 6, ty - 4, tx + tw + 6, ty + 16, color(0xEE, 0x0D, 0x0B, 0x1E));
        border(ctx, tx - 6, ty - 4, tw + 12, 20, color(0x4D, 0x8B, 0x5C, 0xF6));
        text(ctx, "\u2726 " + mod.getName(), tx, ty - 1, TEXT_ACCENT);
        text(ctx, desc,                       tx, ty + 9,  TEXT_DIM);
    }

    // ── Cotton icon ──────────────────────────────────────────────────────────
    private void drawCottonIcon(DrawContext ctx, int x, int y) {
        int c1 = color(0xFF, 0xA7, 0x8B, 0xFA);
        int c2 = color(0xFF, 0x60, 0xA5, 0xFA);
        ctx.fill(x + 2,  y + 16, x + 26, y + 28, c1);
        ctx.fill(x,      y + 8,  x + 14, y + 20, c1);
        ctx.fill(x + 8,  y + 4,  x + 24, y + 18, c2);
        ctx.fill(x + 18, y + 8,  x + 30, y + 20, c2);
        ctx.fill(x + 12, y + 6,  x + 20, y + 12, color(0x66, 0xFF, 0xFF, 0xFF));
    }

    // ── Mouse events ─────────────────────────────────────────────────────────
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int x = guiX, y = guiY;

        // Drag bar (top of main panel)
        if (mx >= x + SIDEBAR_W && mx <= x + GUI_W && my >= y && my <= y + HEADER_H) {
            dragging = true;
            dragOffX = (int) mx - x;
            dragOffY = (int) my - y;
            return true;
        }

        // Sidebar navigation
        int ny = y + 71;
        for (Module.Category cat : Module.Category.values()) {
            if (cat == Module.Category.WORLD)    ny += 15;
            if (cat == Module.Category.SETTINGS) ny += 15;
            if (mx >= x + 6 && mx <= x + SIDEBAR_W - 6 && my >= ny && my <= ny + NAV_ROW_H) {
                selectedCat = cat;
                return true;
            }
            ny += NAV_ROW_H + 2;
        }

        // Module cards
        List<Module> mods = getFilteredModules();
        int pw   = GUI_W - SIDEBAR_W;
        int px   = x + SIDEBAR_W;
        int colW = (pw - 24) / 2;
        int startY = y + HEADER_H + 8;

        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int col = i % 2, row = i / 2;
            int cx  = px + 8 + col * (colW + COL_GAP);
            int cy  = startY + row * (CARD_H + CARD_GAP);

            if (mx >= cx && mx <= cx + colW - 55 && my >= cy && my <= cy + CARD_H) {
                mod.toggle(); return true;
            }
            if (mx >= cx + colW - 36 && mx <= cx + colW - 8 && my >= cy && my <= cy + CARD_H) {
                mod.toggle(); return true;
            }
            if (mx >= cx + colW - 54 && mx <= cx + colW - 37 && my >= cy && my <= cy + CARD_H) {
                mod.setPinned(!mod.isPinned()); return true;
            }
        }

        // Style switcher
        int btnX = x + SIDEBAR_W + 60;
        int barY = y + GUI_H - FOOTER_H + 8;
        for (int i = 0; i < 3; i++) {
            if (mx >= btnX && mx <= btnX + 68 && my >= barY && my <= barY + 22) {
                guiStyle = i; return true;
            }
            btnX += 74;
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging) {
            guiX = (int) MathHelper.clamp(mx - dragOffX, 0, this.width  - GUI_W);
            guiY = (int) MathHelper.clamp(my - dragOffY, 0, this.height - GUI_H);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = false;
        return super.mouseReleased(mx, my, button);
    }

    // ── Keyboard ─────────────────────────────────────────────────────────────
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 || keyCode == 344) { // ESC or Right Shift
            this.close();
            return true;
        }
        if (keyCode == 259 && !searchQuery.isEmpty()) { // Backspace
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (chr >= 32 && searchQuery.length() < 24) {
            searchQuery += chr;
            return true;
        }
        return false;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private List<Module> getFilteredModules() {
        List<Module> base = CottonVisuals.moduleManager.getByCategory(selectedCat);
        if (searchQuery.isEmpty()) return base;
        String q = searchQuery.toLowerCase();
        return base.stream()
                .filter(m -> m.getName().toLowerCase().contains(q)
                          || m.getDescription().toLowerCase().contains(q))
                .toList();
    }
}
