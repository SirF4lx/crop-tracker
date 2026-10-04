package com.croptracker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CropTrackerClient implements ClientModInitializer {
    private static KeyBinding configKey;

    @Override
    public void onInitializeClient() {
        TrackerData.load();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.crop-tracker.configure",
                InputUtil.Type.KEY_SYM,
                GLFW.GLFW_KEY_O,
                "category.crop-tracker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                client.setScreen(new CropTrackerScreen());
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.options.hudHidden) return;

            int x = 10, y = 10, width = 160, height = 55;

            // Background box matching your layout drawing
            context.fill(x, y, x + width, y + height, 0xAA000000);
            context.fillBorder(x, y, x + width, y + height, 0xFF333333);

            // Progress bar
            int bx = x + 8, by = y + 8, bw = width - 16, bh = 14;
            context.fill(bx, by, bx + bw, by + bh, 0xFF555555);

            double progress = 0.0;
            if (TrackerData.goal > 0) {
                progress = (double) TrackerData.currentHarvested / TrackerData.goal;
                if (progress > 1.0) progress = 1.0;
            }

            int filledW = (int) (bw * progress);
            if (filledW > 0) {
                context.fill(bx, by, bx + filledW, by + bh, 0xFF4CAF50);
            }

            // Percentage text
            String pctStr = ((int)(progress * 100)) + "%";
            context.drawTextWithShadow(client.textRenderer, pctStr, x + (width - client.textRenderer.getWidth(pctStr)) / 2, y + 26, 0xFFFFFF);

            // Current / Goal stats text
            String statsStr = String.format("%,d / %,d", TrackerData.currentHarvested, TrackerData.goal);
            context.drawTextWithShadow(client.textRenderer, statsStr, x + (width - client.textRenderer.getWidth(statsStr)) / 2, y + 42, 0xFFFF55);
        });
    }
}