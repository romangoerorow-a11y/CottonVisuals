package dev.cotton.core;

import dev.cotton.gui.ClickGui;
import dev.cotton.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CottonVisuals implements ClientModInitializer {

    public static final String MOD_ID   = "cottonvisuals";
    public static final String MOD_NAME = "Cotton Visuals";
    public static final Logger LOGGER   = LoggerFactory.getLogger(MOD_NAME);

    public static ModuleManager moduleManager;
    public static ClickGui      clickGui;

    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Cotton Visuals initializing...");

        moduleManager = new ModuleManager();
        moduleManager.init();

        // Register keybinding — Right Shift
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cottonvisuals.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.cottonvisuals"
        ));

        // Tick listener — open GUI when key pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new ClickGui());
                }
            }
        });

        LOGGER.info("Cotton Visuals loaded! Press Right Shift to open GUI.");
    }
}
