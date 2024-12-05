package me.youhavetrouble.blockedit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
import java.util.Locale;

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
        List<String> localeFiles;
        try (InputStream in = BlockEdit.class.getClassLoader().getResourceAsStream("locale");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            localeFiles = br.lines().toList();
        } catch (IOException e) {
            plugin.getSLF4JLogger().error("Error loading locale files", e);
            return false;
        }

        Gson gson = new Gson();
        for (String fileName : localeFiles) {
            Locale locale;
            try {
                String localeString = fileName.replace(".json", "");
                String[] split = localeString.split("_");
                if (split.length == 1) {
                    locale = Locale.of(split[0]);
                } else {
                    locale = Locale.of(split[0], split[1]);
                }
            } catch (IllegalArgumentException e) {
                plugin.getSLF4JLogger().error("Invalid locale file name: {}", fileName);
                continue;
            }
            try (InputStream fileStream = BlockEdit.class.getClassLoader().getResourceAsStream("locale/" + fileName);
                JsonReader reader = new JsonReader(new InputStreamReader(fileStream))) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                BELocale beLocale = new BELocale(json);
                BELocale.registerLocale(locale, beLocale);
            } catch (IOException e) {
                plugin.getSLF4JLogger().error("Error reading locale file: {}", fileName, e);
            }
        }
        return true;
    }

}
