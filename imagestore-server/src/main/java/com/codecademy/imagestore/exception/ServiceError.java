package com.codecademy.imagestore.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom service related error enums
 */
public enum ServiceError {

    IMAGE_UPLOAD_FAILED("8080-1001", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image to the system."),
    IMAGE_DATA_NOT_FOUND("8080-1002", HttpStatus.NOT_FOUND, "Image Data not found int the DB for the provided Id: {0}."),

    FAILED_TO_LOAD_FILE("8080-1003", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get the Image(Id: {0}) file from the system."),
    IMAGE_DELETE_FAILED("8080-1004", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete the image from the server due to some error."),
    IMAGE_SIZE_LARGE("8080-1005", HttpStatus.BAD_REQUEST, "The uploaded image is greater than the maximum allowed image size: {0}."),
    IMAGE_UPDATE_FAILED("8080-1006", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update the ImageData(Id: {0}) with latest updated details due to some error."),
    FAILED_TO_ROLL_BACK_PREVIOUS_IMAGE("8080-1007", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to rollback image updated in the file system as the image was not successfully updated in the db."),
    BAD_REQUEST("8080-1008", HttpStatus.BAD_REQUEST, "{0} cannot be null or empty."),
    INVALID_CONTENT_TYPE("8080-1009", HttpStatus.BAD_REQUEST, "Content Type of ImageFile can only be JPEG or PNG."),
    REFRESH_TKN_NOT_FOUND("8080-1010", HttpStatus.NOT_FOUND, "Refresh token not found in the DB."),
    REFRESH_TKN_NOT_CREATED("8080-1011", HttpStatus.NOT_FOUND, "Failed to create refresh token as the user with email: {0} not in DB."),
    REFRESH_TKN_EXPIRED("8080-1012", HttpStatus.UNAUTHORIZED, "Refresh token is expired. Please login again!"),
    ACCESS_TKN_EXPIRED("8080-1013", HttpStatus.UNAUTHORIZED, "Token expired. Need to refresh the token to continue.");

    private final String code;
    private final HttpStatus status;
    private final String details;

    ServiceError(String code, HttpStatus status, String details) {
        this.code = code;
        this.status = status;
        this.details = details;
    }

    public String getCode() {
        return this.code;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getDetails() {
        return this.details;
    }
}
