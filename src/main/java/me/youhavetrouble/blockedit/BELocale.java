package me.youhavetrouble.blockedit;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BELocale {

    private static final Map<Locale, BELocale> locales = new HashMap<>();
    private static final Locale defaultLocale = Locale.ENGLISH;

    public final String COULD_NOT_FIND_WAND_BY_ID;


    protected BELocale(JsonObject json) {
        COULD_NOT_FIND_WAND_BY_ID = json.get("could_not_find_wand_by_id").getAsString();
    }

    protected static void registerLocale(Locale locale, BELocale blockEditLocale) {
        locales.put(locale, blockEditLocale);
        System.out.println("Registered locale " + locale.getISO3Country() + " " + locale.getISO3Language());
    }

    public static BELocale getLocale(@NotNull Locale locale) {
        BELocale beLocale = locales.get(locale);
        if (beLocale == null) beLocale = locales.get(defaultLocale);
        return beLocale;
    }

}
