package dev.cotton.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class DrawHelper {

    // ── Color palette (ARGB) ─────────────────────────────────────────────────
    public static final int BG_DARK        = color(0xEE, 0x0D, 0x0B, 0x14);
    public static final int BG_SIDEBAR     = color(0x1A, 0xFF, 0xFF, 0xFF);
    public static final int BG_PANEL       = color(0x12, 0xFF, 0xFF, 0xFF);
    public static final int BG_CARD        = color(0x0A, 0xFF, 0xFF, 0xFF);
    public static final int BG_CARD_HOVER  = color(0x19, 0xFF, 0xFF, 0xFF);
    public static final int BG_CARD_ON     = color(0x28, 0x1A, 0x0A, 0x2E);
    public static final int BG_NAV_ACTIVE  = color(0x33, 0x8B, 0x5C, 0xF6);

    public static final int ACCENT_PURPLE  = 0xFF8B5CF6;
    public static final int ACCENT_BLUE    = 0xFF60A5FA;
    public static final int ACCENT_LINE    = 0xFF9966FF;
    public static final int ACCENT_GOLD    = 0xFFFFC832;

    public static final int TEXT_PRIMARY   = color(0xEB, 0xFF, 0xFF, 0xFF);
    public static final int TEXT_SECONDARY = color(0x80, 0xFF, 0xFF, 0xFF);
    public static final int TEXT_DIM       = color(0x4D, 0xFF, 0xFF, 0xFF);
    public static final int TEXT_PURPLE    = 0xFFC4BBFF;
    public static final int TEXT_ACCENT    = 0xFFD4BBFF;
    public static final int TEXT_HINT      = color(0x33, 0xFF, 0xFF, 0xFF);

    public static final int BORDER_SUBTLE  = color(0x15, 0xFF, 0xFF, 0xFF);
    public static final int BORDER_ACTIVE  = color(0x66, 0x8B, 0x5C, 0xF6);
    public static final int BORDER_GOLD    = color(0x66, 0xFF, 0xC8, 0x32);

    public static final int TOGGLE_ON_L    = 0xFF8B5CF6;
    public static final int TOGGLE_ON_R    = 0xFF60A5FA;
    public static final int TOGGLE_OFF     = color(0x33, 0xFF, 0xFF, 0xFF);
    public static final int TOGGLE_DOT     = 0xFFFFFFFF;

    // ── Color helper ─────────────────────────────────────────────────────────
    public static int color(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    // ── Draw primitives ──────────────────────────────────────────────────────

    public static void border(DrawContext ctx, int x, int y, int w, int h, int col) {
        ctx.fill(x,         y,         x + w,     y + 1,     col);
        ctx.fill(x,         y + h - 1, x + w,     y + h,     col);
        ctx.fill(x,         y,         x + 1,     y + h,     col);
        ctx.fill(x + w - 1, y,         x + w,     y + h,     col);
    }

    public static void accentBar(DrawContext ctx, int x, int y, int h, int col) {
        ctx.fill(x, y, x + 2, y + h, col);
    }

    public static void divider(DrawContext ctx, int x, int y, int w) {
        ctx.fill(x, y, x + w, y + 1, BORDER_SUBTLE);
    }

    public static void toggle(DrawContext ctx, int x, int y, boolean on) {
        int W = 28, H = 15;
        int trackColor = on ? TOGGLE_ON_L : TOGGLE_OFF;
        ctx.fill(x, y, x + W, y + H, trackColor);
        int dotX = on ? x + W - H + 2 : x + 2;
        ctx.fill(dotX, y + 2, dotX + H - 4, y + H - 2, TOGGLE_DOT);
    }

    public static void moduleCard(DrawContext ctx, int x, int y, int w, int h,
                                   boolean on, boolean pinned, boolean hovered) {
        int bg = on ? BG_CARD_ON : (hovered ? BG_CARD_HOVER : BG_CARD);
        ctx.fill(x, y, x + w, y + h, bg);
        int borderCol = pinned ? BORDER_GOLD : (on ? BORDER_ACTIVE : BORDER_SUBTLE);
        border(ctx, x, y, w, h, borderCol);
        if (on) accentBar(ctx, x, y, h, ACCENT_LINE);
    }

    // ── Text helpers ─────────────────────────────────────────────────────────

    public static void text(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, false);
    }

    public static void textShadow(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }

    public static void textCentered(DrawContext ctx, String text, int cx, int y, int color) {
        var tr = MinecraftClient.getInstance().textRenderer;
        int tw = tr.getWidth(text);
        ctx.drawText(tr, text, cx - tw / 2, y, color, false);
    }

    public static String truncate(String s, int maxChars) {
        if (s.length() <= maxChars) return s;
        return s.substring(0, maxChars - 2) + "..";
    }
}
