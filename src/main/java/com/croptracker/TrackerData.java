package com.croptracker;

import net.minecraft.client.MinecraftClient;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class TrackerData {
    public static String targetCrop = "potato";
    public static int goal = 1000000;
    public static int currentHarvested = 500000;

    private static File getConfigFile() {
        return new File(MinecraftClient.getInstance().runDirectory, "config/croptracker.properties");
    }

    public static void load() {
        File file = getConfigFile();
        if (!file.exists()) return;
        Properties props = new Properties();
        try (FileReader reader = new FileReader(file)) {
            props.load(reader);
            targetCrop = props.getProperty("targetCrop", "potato");
            goal = Integer.parseInt(props.getProperty("goal", "1000000"));
            currentHarvested = Integer.parseInt(props.getProperty("currentHarvested", "0"));
        } catch (Exception e) {}
    }

    public static void save() {
        File file = getConfigFile();
        file.getParentFile().mkdirs();
        Properties props = new Properties();
        props.setProperty("targetCrop", targetCrop);
        props.setProperty("goal", String.valueOf(goal));
        props.setProperty("currentHarvested", String.valueOf(currentHarvested));
        try (FileWriter writer = new FileWriter(file)) {
            props.store(writer, "Crop Tracker Config");
        } catch (IOException e) {}
    }
}