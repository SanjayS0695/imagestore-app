package com.codecademy.imagestore.validator;

import com.codecademy.imagestore.exception.GenericAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public class ImageStoreValidator {

    private static final String JPEG_TYPE = "image/jpeg";
    private static final String PNG_TYPE = "image/png";

    /**
     * Method to validate MultipartFile image
     *
     * @param image
     */
    public static void validateImage(final MultipartFile image) {
        if (image == null) {
            throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Uploaded image cannot be null or empty.");
        } else {
            final String contentType = image.getContentType();
            if (null == contentType || contentType.isBlank()) {
                throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Content Type of the uploaded file cannot be null or empty.");
            } else {
                if (!(contentType.equals(JPEG_TYPE) || contentType.equals(PNG_TYPE))) {
                    throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Uploaded image should only be of the format JPEG or PNG.");
                }
                if (null == image.getOriginalFilename()) {
                    throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Image filename cannot be null or empty.");
                }
            }
        }
    }

    /**
     * Method to validate Id
     *
     * @param id
     */
    public static void validateId(Long id) {
        if (null == id) {
            throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Image id cannot be null.");
        }
    }
}
