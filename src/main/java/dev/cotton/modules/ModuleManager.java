package dev.cotton.modules;

import dev.cotton.core.Module;
import dev.cotton.modules.visuals.*;
import dev.cotton.modules.hud.*;
import dev.cotton.modules.particles.*;
import dev.cotton.modules.world.*;
import dev.cotton.modules.sounds.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // Visuals
        register(new JumpCircles());
        register(new Trails());
        register(new BlockHighlight());
        register(new BetterGlow());
        register(new HandShader());
        register(new TargetRender());
        register(new NoFire());

        // HUD
        register(new Watermark());
        register(new PotionsHud());
        register(new PlayerInfo());
        register(new ArmorHud());
        register(new Notifications());

        // Particles
        register(new HitParticles());
        register(new HitBubbles());
        register(new TotemParticles());
        register(new AmbientParticles());
        register(new KillEffect());

        // World
        register(new Bright());
        register(new CustomFog());
        register(new CustomTime());
        register(new CustomLight());

        // Sounds
        register(new KillSound());
        register(new UiClicks());
    }

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }

    public Module getByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
