package com.codecademy.imagestore.enums;

import java.util.Arrays;

public enum ImageType {
    JPEG("image/jpeg"),
    PNG("image/png");

    private final String label;

    ImageType(String label) {
        this.label = label;
    }

    private String getLabel() {
        return this.label;
    }

    public static ImageType getImageType(String label) {
        return Arrays.stream(ImageType.values())
                .filter(imageType -> imageType.getLabel().equals(label))
                .findFirst().orElse(null);
    }
}
