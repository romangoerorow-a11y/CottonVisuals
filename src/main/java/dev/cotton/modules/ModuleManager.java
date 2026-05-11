package dev.cotton.modules;

import dev.cotton.core.Module;
import dev.cotton.modules.hud.*;
import dev.cotton.modules.particles.*;
import dev.cotton.modules.sounds.*;
import dev.cotton.modules.visuals.*;
import dev.cotton.modules.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // ── Visuals ──────────────────────────────────────────────────────────
        register(new JumpCircles());
        register(new Trails());
        register(new BlockHighlight());
        register(new BetterGlow());
        register(new HandShader());
        register(new TargetRender());
        register(new Hitboxes());
        register(new NoFire());

        // ── HUD ──────────────────────────────────────────────────────────────
        register(new Watermark());
        register(new PotionsHud());
        register(new CooldownsHud());
        register(new PlayerInfo());
        register(new HotbarHud());
        register(new TargetHud());
        register(new ArmorHud());
        register(new Notifications());

        // ── Particles ────────────────────────────────────────────────────────
        register(new HitParticles());
        register(new HitBubbles());
        register(new TotemParticles());
        register(new AmbientParticles());
        register(new Firefly());
        register(new KillEffect());

        // ── World ────────────────────────────────────────────────────────────
        register(new Bright());
        register(new CustomFog());
        register(new CustomTime());
        register(new CustomLight());

        // ── Sounds ───────────────────────────────────────────────────────────
        register(new KillSound());
        register(new UiClicks());
    }

    private void register(Module m) { modules.add(m); }

    public List<Module> getAll() { return modules; }

    public List<Module> getByCategory(Module.Category cat) {
        return modules.stream()
                .filter(m -> m.getCategory() == cat)
                .collect(Collectors.toList());
    }

    public Module getByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
