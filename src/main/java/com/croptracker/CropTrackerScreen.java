package com.croptracker;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CropTrackerScreen extends Screen {
    private TextFieldWidget cropField;
    private TextFieldWidget goalField;
    private TextFieldWidget currentField;

    public CropTrackerScreen() {
        super(Text.literal("Crop Tracker Settings"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        this.cropField = new TextFieldWidget(this.textRenderer, cx - 100, cy - 50, 200, 20, Text.literal("Crop Name"));
        this.cropField.setText(TrackerData.targetCrop);
        this.addDrawableChild(this.cropField);

        this.goalField = new TextFieldWidget(this.textRenderer, cx - 100, cy - 15, 200, 20, Text.literal("Goal Amount"));
        this.goalField.setText(String.valueOf(TrackerData.goal));
        this.addDrawableChild(this.goalField);

        this.currentField = new TextFieldWidget(this.textRenderer, cx - 100, cy + 20, 200, 20, Text.literal("Current Harvested"));
        this.currentField.setText(String.valueOf(TrackerData.currentHarvested));
        this.addDrawableChild(this.currentField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save & Close"), btn -> {
            TrackerData.targetCrop = this.cropField.getText().trim().toLowerCase();
            try { TrackerData.goal = Integer.parseInt(this.goalField.getText().trim()); } catch (Exception e) {}
            try { TrackerData.currentHarvested = Integer.parseInt(this.currentField.getText().trim()); } catch (Exception e) {}
            TrackerData.save();
            this.close();
        }).dimensions(cx - 100, cy + 55, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Crop ID (potato, carrot, wheat...):", this.width / 2 - 100, this.height / 2 - 63, 0xAAAAAA);
        context.drawTextWithShadow(this.textRenderer, "Goal Amount:", this.width / 2 - 100, this.height / 2 - 28, 0xAAAAAA);
        context.drawTextWithShadow(this.textRenderer, "Current Harvested:", this.width / 2 - 100, this.height / 2 + 7, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
}