package dev.cotton.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

/**
 * Cotton Visuals – Draw Helper
 * Provides rounded rectangles, gradients, toggles and other
 * UI primitives used by the Click GUI.
 */
public class DrawHelper {

    // ── Color palette (ARGB) ─────────────────────────────────────────────────
    public static final int BG_DARK        = color(0xEE, 0x0D, 0x0B, 0x14); // near-black
    public static final int BG_SIDEBAR     = color(0x1A, 0xFF, 0xFF, 0xFF); // white 10%
    public static final int BG_PANEL       = color(0x12, 0xFF, 0xFF, 0xFF); // white 7%
    public static final int BG_CARD        = color(0x0A, 0xFF, 0xFF, 0xFF); // white 4%
    public static final int BG_CARD_HOVER  = color(0x19, 0xFF, 0xFF, 0xFF); // white 10%
    public static final int BG_CARD_ON     = color(0x28, 0x1A, 0x0A, 0x2E); // dark purple
    public static final int BG_NAV_ACTIVE  = color(0x33, 0x8B, 0x5C, 0xF6); // purple 20%

    public static final int ACCENT_PURPLE  = 0xFF8B5CF6;
    public static final int ACCENT_BLUE    = 0xFF60A5FA;
    public static final int ACCENT_LINE    = 0xFF9966FF;
    public static final int ACCENT_GOLD    = 0xFFFFC832;

    public static final int TEXT_PRIMARY   = color(0xEB, 0xFF, 0xFF, 0xFF); // 92%
    public static final int TEXT_SECONDARY = color(0x80, 0xFF, 0xFF, 0xFF); // 50%
    public static final int TEXT_DIM       = color(0x4D, 0xFF, 0xFF, 0xFF); // 30%
    public static final int TEXT_PURPLE    = 0xFFC4BBFF;
    public static final int TEXT_ACCENT    = 0xFFD4BBFF;
    public static final int TEXT_HINT      = color(0x33, 0xFF, 0xFF, 0xFF); // 20%

    public static final int BORDER_SUBTLE  = color(0x15, 0xFF, 0xFF, 0xFF); // white 8%
    public static final int BORDER_ACTIVE  = color(0x66, 0x8B, 0x5C, 0xF6); // purple 40%
    public static final int BORDER_GOLD    = color(0x66, 0xFF, 0xC8, 0x32);  // gold 40%

    public static final int TOGGLE_ON_L    = 0xFF8B5CF6;
    public static final int TOGGLE_ON_R    = 0xFF60A5FA;
    public static final int TOGGLE_OFF     = color(0x33, 0xFF, 0xFF, 0xFF);
    public static final int TOGGLE_DOT     = 0xFFFFFFFF;

    // ────────────────────────────────────────────────────────────────────────

    /** ARGB helper */
    public static int color(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Thin 1-pixel border around a rect */
    public static void border(DrawContext ctx, int x, int y, int w, int h, int col) {
        ctx.fill(x,         y,         x + w,     y + 1,     col); // top
        ctx.fill(x,         y + h - 1, x + w,     y + h,     col); // bottom
        ctx.fill(x,         y,         x + 1,     y + h,     col); // left
        ctx.fill(x + w - 1, y,         x + w,     y + h,     col); // right
    }

    /** Left accent bar (2 px) */
    public static void accentBar(DrawContext ctx, int x, int y, int h, int col) {
        ctx.fill(x, y, x + 2, y + h, col);
    }

    /**
     * Simulate a rounded rectangle by:
     * 1. Drawing the main fill
     * 2. Darkening the 4 corner pixels to "clip" them
     */
    public static void roundRect(DrawContext ctx, int x, int y, int w, int h, int radius, int fill) {
        // Main body
        ctx.fill(x + radius, y,          x + w - radius, y + h,         fill);
        ctx.fill(x,          y + radius, x + radius,     y + h - radius, fill);
        ctx.fill(x + w - radius, y + radius, x + w,      y + h - radius, fill);

        // Corner triangles (approximate with small squares)
        for (int r = 1; r <= radius; r++) {
            int len = radius - r + 1;
            ctx.fill(x + radius - r, y + radius - r,
                     x + radius - r + 1, y + radius - r + 1, fill);
            ctx.fill(x + w - radius + r - 1, y + radius - r,
                     x + w - radius + r, y + radius - r + 1, fill);
            ctx.fill(x + radius - r, y + h - radius + r - 1,
                     x + radius - r + 1, y + h - radius + r, fill);
            ctx.fill(x + w - radius + r - 1, y + h - radius + r - 1,
                     x + w - radius + r, y + h - radius + r, fill);
        }
    }

    /** Gradient fill – top to bottom */
    public static void gradientV(DrawContext ctx, int x, int y, int w, int h, int top, int bot) {
        ctx.fillGradient(x, y, x + w, y + h, top, bot);
    }

    /** Gradient fill – left to right using two vertical halves */
    public static void gradientH(DrawContext ctx, int x, int y, int w, int h, int left, int right) {
        // Approximate: use fillGradient with Axis
        // In 1.21.4 the overload is fillGradient(x1,y1,x2,y2,z,colorStart,colorEnd)
        // We rotate: split vertically and use the same color per column isn't easily doable
        // Simple approach: just use left color (placeholder until shader)
        ctx.fill(x, y, x + w, y + h, left);
    }

    /**
     * Draw a nice toggle switch.
     * @param on   state
     * @param x,y  top-left of the toggle
     */
    public static void toggle(DrawContext ctx, int x, int y, boolean on) {
        int W = 28, H = 15;
        // Track
        if (on) {
            gradientH(ctx, x, y, W, H, TOGGLE_ON_L, TOGGLE_ON_R);
        } else {
            ctx.fill(x, y, x + W, y + H, TOGGLE_OFF);
        }
        // Dot
        int dotX = on ? x + W - H + 2 : x + 2;
        ctx.fill(dotX, y + 2, dotX + H - 4, y + H - 2, TOGGLE_DOT);
    }

    /**
     * Draw a module card background with optional pin/active styles.
     */
    public static void moduleCard(DrawContext ctx, int x, int y, int w, int h,
                                   boolean on, boolean pinned, boolean hovered) {
        int bg = on ? BG_CARD_ON : (hovered ? BG_CARD_HOVER : BG_CARD);
        ctx.fill(x, y, x + w, y + h, bg);
        // Border
        int border = pinned ? BORDER_GOLD : (on ? BORDER_ACTIVE : BORDER_SUBTLE);
        border(ctx, x, y, w, h, border);
        // Left accent when on
        if (on) accentBar(ctx, x, y, h, ACCENT_LINE);
    }

    /**
     * Draw a compact horizontal divider line.
     */
    public static void divider(DrawContext ctx, int x, int y, int w) {
        ctx.fill(x, y, x + w, y + 1, BORDER_SUBTLE);
    }

    /** Draw centered text */
    public static void textCentered(DrawContext ctx, String text, int cx, int y, int color) {
        var tr = MinecraftClient.getInstance().textRenderer;
        int tw = tr.getWidth(text);
        ctx.drawText(tr, text, cx - tw / 2, y, color, false);
    }

    /** Draw text */
    public static void text(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, false);
    }

    /** Draw text with shadow */
    public static void textShadow(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }

    /** Truncate string to maxChars, append ".." if needed */
    public static String truncate(String s, int maxChars) {
        if (s.length() <= maxChars) return s;
        return s.substring(0, maxChars - 2) + "..";
    }
}
