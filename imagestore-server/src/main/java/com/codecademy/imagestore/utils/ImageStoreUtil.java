package com.codecademy.imagestore.utils;

import java.util.Objects;
import java.util.UUID;

public class ImageStoreUtil {

    private static final String extPattern = "(?<!^)[.].*";

    public static String generateFilePath(String basePath, String filename) {
        UUID uuid = UUID.randomUUID();
        return String.format("%s%s_%s",basePath,uuid,filename);
    }

    public static String getFilenameWithoutExtension(String filename) {
        return Objects.requireNonNull(filename).replaceAll(extPattern, "");
    }
}
