package me.youhavetrouble.blockedit;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BELocale {

    private static final Map<Locale, BELocale> locales = new HashMap<>();
    private static final Locale defaultLocale = Locale.of("en_US");

    public final String couldNotFindWandById, selectArea, copiedSelectionToClipboard, selectionReset, firstPositionSet,
            secondPositionSet, pastingClipboard, clipboardRotated, settingBlocks, replacingBlocks;


    protected BELocale(JsonObject json) {
        couldNotFindWandById = json.get("could_not_find_wand_by_id").getAsString();
        selectArea = json.get("select_area").getAsString();
        copiedSelectionToClipboard = json.get("copied_selection_to_clipboard").getAsString();
        selectionReset = json.get("selection_reset").getAsString();
        firstPositionSet = json.get("first_position_set").getAsString();
        secondPositionSet = json.get("second_position_set").getAsString();
        pastingClipboard = json.get("pasting_clipboard").getAsString();
        clipboardRotated = json.get("clipboard_rotated").getAsString();
        settingBlocks = json.get("setting_blocks").getAsString();
        replacingBlocks = json.get("replacing_blocks").getAsString();
    }

    protected static void registerLocale(Locale locale, BELocale blockEditLocale) {
        locales.put(locale, blockEditLocale);
    }

    public static BELocale getLocale(@NotNull Locale locale) {
        BELocale beLocale = locales.get(locale);
        if (beLocale == null) beLocale = locales.get(defaultLocale);
        return beLocale;
    }

}
