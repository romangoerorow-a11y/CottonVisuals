package dev.cotton.modules.visuals;

import dev.cotton.core.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class Trails extends Module {

    private Vec3d lastPos = null;

    public Trails() {
        super("Trails", "Colorful particle trail as you move", Category.VISUALS);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        Vec3d pos = mc.player.getPos();

        if (lastPos != null && lastPos.distanceTo(pos) > 0.2) {
            // Spawn trail particles
            for (int i = 0; i < 3; i++) {
                double offsetX = (Math.random() - 0.5) * 0.3;
                double offsetZ = (Math.random() - 0.5) * 0.3;
                mc.world.addParticle(
                        ParticleTypes.END_ROD,
                        pos.x + offsetX,
                        pos.y + 0.1,
                        pos.z + offsetZ,
                        0, -0.02, 0
                );
            }
        }

        lastPos = pos;
    }

    @Override
    public void onDisable() {
        lastPos = null;
    }
}
