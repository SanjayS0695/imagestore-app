package com.codecademy.imagestore.validator;

import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.exception.ServiceError;
import org.springframework.web.multipart.MultipartFile;

import static com.codecademy.imagestore.utils.ServiceConstants.IMAGE_JPEG;
import static com.codecademy.imagestore.utils.ServiceConstants.IMAGE_PNG;

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
            throw new GenericAPIException(ServiceError.BAD_REQUEST, "ImageFile");
        } else {
            final String contentType = image.getContentType();
            if (null == contentType || contentType.isBlank()) {
                throw new GenericAPIException(ServiceError.BAD_REQUEST, "Content Type of file");
            } else {
                if (!(contentType.equals(IMAGE_JPEG) || contentType.equals(IMAGE_PNG))) {
                    throw new GenericAPIException(ServiceError.INVALID_CONTENT_TYPE);
                }
                if (null == image.getOriginalFilename()) {
                    throw new GenericAPIException(ServiceError.BAD_REQUEST, "Filename");
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
            throw new GenericAPIException(ServiceError.BAD_REQUEST, "Image Id");
        }
    }
}
