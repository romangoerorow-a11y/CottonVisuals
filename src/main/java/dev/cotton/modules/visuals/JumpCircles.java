package dev.cotton.modules.visuals;

import dev.cotton.core.Module;
import dev.cotton.core.CottonVisuals;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class JumpCircles extends Module {

    private boolean wasOnGround = true;
    private float circleAlpha = 0f;
    private Vec3d circlePos = null;
    private float circleRadius = 0f;

    public JumpCircles() {
        super("Jump Circles", "Glowing ring on every jump", Category.VISUALS);
    }

    @Override
    public void onEnable() {
        CottonVisuals.LOGGER.info("[Cotton] Jump Circles enabled");
    }

    @Override
    public void onDisable() {
        CottonVisuals.LOGGER.info("[Cotton] Jump Circles disabled");
        circleAlpha = 0f;
        circlePos = null;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        PlayerEntity player = mc.player;
        boolean onGround = player.isOnGround();

        // Detect jump: was on ground, now not on ground, and moving up
        if (wasOnGround && !onGround && player.getVelocity().y > 0) {
            circlePos = player.getPos();
            circleAlpha = 1.0f;
            circleRadius = 0f;
        }

        // Animate circle
        if (circleAlpha > 0) {
            circleAlpha -= 0.04f;
            circleRadius += 0.15f;
            if (circleAlpha < 0) circleAlpha = 0;
        }

        wasOnGround = onGround;
    }

    public float getCircleAlpha() { return circleAlpha; }
    public float getCircleRadius() { return circleRadius; }
    public Vec3d getCirclePos() { return circlePos; }
}
