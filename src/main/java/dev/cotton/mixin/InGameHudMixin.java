package dev.cotton.mixin;

import dev.cotton.core.CottonVisuals;
import dev.cotton.core.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (CottonVisuals.moduleManager == null) return;

        Module wm = CottonVisuals.moduleManager.getByName("Watermark");
        if (wm != null && wm.isEnabled()) {
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                "\u2726 Cotton Visuals",
                4, 4, 0xFFD4BBFF, true
            );
        }
    }
}
