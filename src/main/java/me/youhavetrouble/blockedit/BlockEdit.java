package me.youhavetrouble.blockedit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class BlockEdit extends JavaPlugin {

    private static BlockEdit plugin;
    private static SchematicHandler schematicHandler;
    private static WandsHandler wandsHandler;

    @Override
    public void onEnable() {
        plugin = this;

        if (!initLocales()) {
            plugin.getSLF4JLogger().error("Could not load locale files");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        schematicHandler = new SchematicHandler(this);
        wandsHandler = new WandsHandler(this);

        SelectionWand selectionWand = new SelectionWand();
        wandsHandler.registerWand(selectionWand);
        getServer().getPluginManager().registerEvents(selectionWand, this);

        BlockEditCommands.registerCommands(this);
    }

    public static BlockEdit getPlugin() {
        return plugin;
    }

    public static SchematicHandler getSchematicHandler() {
        return schematicHandler;
    }

    public static WandsHandler getWandsHandler() {
        return wandsHandler;
    }

    private boolean initLocales() {
        Reflections reflections = new Reflections("locale", Scanners.Resources);
        Set<String> fileNames = reflections.getResources(Pattern.compile("([a-zA-Z]{1,3}_[a-zA-Z]{1,3})(\\.json)"));

        Gson gson = new Gson();
        for (String fileName : fileNames) {
            Locale locale;
            try {
                String localeString = fileName
                        .replace(".json", "")
                        .replace("locale/", "");
                locale = Locale.of(localeString);
            } catch (IllegalArgumentException e) {
                plugin.getSLF4JLogger().error("Invalid locale file name: {}", fileName);
                continue;
            }
            String resourcePath = "/" + fileName;
            try (InputStream fileStream = BlockEdit.class.getResourceAsStream(resourcePath)) {
                if (fileStream == null) {
                    plugin.getSLF4JLogger().error("Locale file not found: {}", resourcePath);
                    continue;
                }
                JsonReader reader = new JsonReader(new InputStreamReader(fileStream));
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                BELocale beLocale = new BELocale(json);
                BELocale.registerLocale(locale, beLocale);
            } catch (IOException e) {
                plugin.getSLF4JLogger().error("Error reading locale file: {}", resourcePath, e);
            }
        }
        return true;
    }

}
