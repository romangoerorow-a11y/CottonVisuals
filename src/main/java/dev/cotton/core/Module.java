package dev.cotton.core;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }

    public enum Category {
        VISUALS("✦ Visuals"),
        HUD("◈ HUD"),
        PARTICLES("✧ Particles"),
        WORLD("◉ World"),
        SOUNDS("♪ Sounds");

        private final String displayName;
        Category(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
