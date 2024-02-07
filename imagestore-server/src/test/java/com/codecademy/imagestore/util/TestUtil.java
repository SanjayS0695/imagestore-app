package com.codecademy.imagestore.util;

import com.codecademy.imagestore.entity.ImageData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class TestUtil {

    public static final String REF_IMAGE_PATH = "src/test/resources/testdata/refImage.png";
    public static final String TEST_IMAGE_PATH = "src/test/resources/imagestore/testimage.png";

    public static final String TEST_IMAGE_ROOT = "src/test/resources/imagestore/";


    public static void loadImageFromTestData() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        try (FileOutputStream outputStream = new FileOutputStream(TEST_IMAGE_PATH)) {
            outputStream.write(byteStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getCurrentID(List<ImageData> list) {
        var entry = list.stream().findFirst();
        return entry.map(ImageData::getId).orElse(null);
    }
}
