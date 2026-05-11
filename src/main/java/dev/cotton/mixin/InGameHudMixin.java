package dev.cotton.mixin;

import dev.cotton.core.CottonVisuals;
import dev.cotton.core.Module;
import dev.cotton.modules.hud.Watermark;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (CottonVisuals.moduleManager == null) return;
        // Draw Watermark if enabled
        Module wm = CottonVisuals.moduleManager.getByName("Watermark");
        if (wm != null && wm.isEnabled()) {
            context.drawText(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                "✦ Cotton Visuals",
                4, 4, 0xFFD4BBFF, true
            );
        }
    }
}
