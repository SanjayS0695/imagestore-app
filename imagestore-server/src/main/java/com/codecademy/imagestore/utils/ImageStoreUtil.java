package com.codecademy.imagestore.utils;

import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.exception.ServiceError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class ImageStoreUtil {

    private static final String extPattern = "(?<!^)[.].*";

    /**
     * Method to generate filePath with the filename and random UUID to maintain distinction between files
     *
     * @param basePath
     * @param filename
     * @return
     */
    public static String generateFilePath(String basePath, String filename) {
        UUID uuid = UUID.randomUUID();
        return String.format("%s%s_%s",basePath,uuid,filename);
    }

    /**
     * Method to get the filename without its extension
     *
     * @param filename
     * @return
     */
    public static String getFilenameWithoutExtension(String filename) {
        return Objects.requireNonNull(filename).replaceAll(extPattern, "");
    }

    /**
     * Method to write to File
     *
     * @param path
     * @param oldImage
     */
    public static void writeBytesToFile(String path, File oldImage) {
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            var byteStream = readBytesFromFilePath(oldImage.getPath());
            outputStream.write(byteStream);
        } catch (IOException e) {
            throw new GenericAPIException(ServiceError.FAILED_TO_ROLL_BACK_PREVIOUS_IMAGE);
        }
    }

    public static byte[] readBytesFromFilePath(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }
}

