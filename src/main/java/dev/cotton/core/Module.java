package dev.cotton.core;

public abstract class Module {

    public enum Category {
        VISUALS("✦ Visuals"),
        HUD("◈ HUD"),
        PARTICLES("✧ Particles"),
        WORLD("◉ World"),
        SOUNDS("♪ Sounds"),
        SETTINGS("⚙ Settings");

        private final String displayName;
        Category(String name) { this.displayName = name; }
        public String getDisplayName() { return displayName; }
    }

    private final String   name;
    private final String   description;
    private final Category category;
    private boolean        enabled;
    private int            keyBind = -1;   // GLFW key code, -1 = none
    private boolean        pinned  = false;

    public Module(String name, String description, Category category) {
        this.name        = name;
        this.description = description;
        this.category    = category;
        this.enabled     = false;
    }

    public Module(String name, String description, Category category, boolean defaultOn) {
        this(name, description, category);
        this.enabled = defaultOn;
    }

    // Called every tick when enabled – override in subclass
    public void onTick() {}
    // Called when toggled – override in subclass
    public void onEnable()  {}
    public void onDisable() {}

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public void setEnabled(boolean v) {
        if (v != enabled) toggle();
    }

    public String   getName()        { return name;        }
    public String   getDescription() { return description; }
    public Category getCategory()    { return category;    }
    public boolean  isEnabled()      { return enabled;     }
    public int      getKeyBind()     { return keyBind;     }
    public boolean  isPinned()       { return pinned;      }
    public void     setKeyBind(int k){ keyBind = k;        }
    public void     setPinned(boolean p) { pinned = p;     }
}
