package me.youhavetrouble.blockedit.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class NameFilenameFilter implements FilenameFilter {

    private final String filterName;

    public NameFilenameFilter(@NotNull String name) {
        this.filterName = name;
    }

    @Override
    public boolean accept(File dir, String name) {

        String[] parts = name.split("\\.");
        if (parts.length < 2) return filterName.equals(name);
        String joined = String.join(".", List.of(parts).subList(0, parts.length - 1));
        return joined.equals(filterName);
    }
}
