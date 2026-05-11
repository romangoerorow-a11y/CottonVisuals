package dev.cotton.mixin;

import dev.cotton.core.CottonVisuals;
import dev.cotton.core.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (CottonVisuals.moduleManager == null) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        // Watermark
        Module watermarkMod = CottonVisuals.moduleManager.getByName("Watermark");
        if (watermarkMod != null && watermarkMod.isEnabled()) {
            context.fill(4, 4, 98, 22, 0xAA0D0B18);
            context.fill(4, 4, 5, 22, 0xFF9966FF);
            context.drawTextWithShadow(
                    mc.textRenderer,
                    Text.literal("✦ Cotton Visuals"),
                    8, 8, 0xFFD4BBFF
            );
        }

        // Player Info
        Module playerInfoMod = CottonVisuals.moduleManager.getByName("Player Info");
        if (playerInfoMod != null && playerInfoMod.isEnabled()) {
            String infoText = "FPS: " + mc.getCurrentFps();
            int screenW = context.getScaledWindowWidth();
            int textW = mc.textRenderer.getWidth(infoText);
            context.fill(screenW - textW - 12, 4, screenW - 4, 14, 0xAA0D0B18);
            context.drawTextWithShadow(mc.textRenderer, Text.literal(infoText),
                    screenW - textW - 8, 6, 0xFFAA88FF);
        }

        // Tick modules
        for (Module m : CottonVisuals.moduleManager.getModules()) {
            if (m.isEnabled()) m.onTick();
        }
    }
}
