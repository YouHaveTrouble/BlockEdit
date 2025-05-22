package me.youhavetrouble.blockedit;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BELocale {

    private static final Map<Locale, BELocale> locales = new HashMap<>();
    private static final Locale defaultLocale = Locale.of("en_US");

    public final String couldNotFindWandById, selectArea, copiedSelectionToClipboard, selectionReset, firstPositionSet,
            secondPositionSet, pastingClipboard, clipboardRotated, settingBlocks, replacingBlocks, schematicLoaded,
            startedLoadingSchematic, noProviderForSchematicFileExtension, schematicLoadError, schematicNotFound,
            providerNotFound;


    protected BELocale(JsonObject json) {
        couldNotFindWandById = getString(json, "could_not_find_wand_by_id");
        selectArea = getString(json, "select_area");
        copiedSelectionToClipboard = getString(json, "copied_selection_to_clipboard");
        selectionReset = getString(json, "selection_reset");
        firstPositionSet = getString(json, "first_position_set");
        secondPositionSet = getString(json, "second_position_set");
        pastingClipboard = getString(json, "pasting_clipboard");
        clipboardRotated = getString(json, "clipboard_rotated");
        settingBlocks = getString(json, "setting_blocks");
        replacingBlocks = getString(json, "replacing_blocks");
        startedLoadingSchematic = getString(json, "started_loading_schematic");
        schematicLoaded = getString(json, "schematic_loaded");
        noProviderForSchematicFileExtension = getString(json, "no_provider_for_schematic_file_extension");
        schematicLoadError = getString(json, "schematic_load_error");
        schematicNotFound = getString(json, "schematic_not_found");
        providerNotFound = getString(json, "provider_not_found");
    }

    @Nullable
    private String getString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsString();
        } else {
            return null;
        }
    }

    protected static void registerLocale(Locale locale, BELocale blockEditLocale) {
        locales.put(locale, blockEditLocale);
    }

    public static BELocale getLocale(@Nullable Locale locale) {
        if (locale == null) return locales.get(defaultLocale);
        BELocale beLocale = locales.get(locale);
        if (beLocale == null) beLocale = locales.get(defaultLocale);
        return beLocale;
    }

}
