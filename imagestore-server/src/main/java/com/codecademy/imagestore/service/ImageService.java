package com.codecademy.imagestore.service;

import com.codecademy.imagestore.dto.AggregatedImagesDTO;
import com.codecademy.imagestore.dto.ImageDataDTO;
import com.codecademy.imagestore.entity.ImageData;
import com.codecademy.imagestore.enums.ImageType;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.exception.ServiceError;
import com.codecademy.imagestore.mapper.ImageDataMapper;
import com.codecademy.imagestore.repository.ImageRepository;
import com.codecademy.imagestore.utils.ImageStoreUtil;
import com.codecademy.imagestore.validator.ImageStoreValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@CacheConfig(cacheNames = "image_data")
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${image-store.filepath}")
    private String basePath;

    @Value("${image-store.maximum-file-size:0}")
    private String maximumImageSize;

    /**
     * Method to upload Image to the system
     *
     * @param imageFile
     * @return
     */
    public Long uploadImage(MultipartFile imageFile, String description) {
        ImageStoreValidator.validateImage(imageFile);
        validateImageSize(imageFile.getSize());

        var fileName = ImageStoreUtil.getFilenameWithoutExtension(imageFile.getOriginalFilename());
        var completePath = ImageStoreUtil.generateFilePath(basePath, imageFile.getOriginalFilename());
        var imageData = new ImageData();
        imageData.setFilePath(completePath);
        imageData.setName(fileName);
        if (description != null && !description.isBlank()) {
            imageData.setDescription(description);
        }
        imageData.setType(ImageType.getImageType(imageFile.getContentType()));

        try {
            imageFile.transferTo(Path.of(completePath));
            log.info("[ImageStore - Upload Image] - Image was successfully saved to the file system.");
            try {
                var savedImageData = this.imageRepository.save(imageData);
                log.info("[ImageStore - Upload Image] - ImageData was successfully saved to the DB.");
                return savedImageData.getId();
            } catch (Exception e) {
                var image = new File(imageData.getFilePath());
                image.delete();
                log.error(ServiceError.IMAGE_UPLOAD_FAILED.getDetails(), e);
                throw new GenericAPIException(ServiceError.IMAGE_UPLOAD_FAILED);
            }
        } catch (Exception e) {
            log.error(ServiceError.IMAGE_UPLOAD_FAILED.getDetails(), e);
            throw new GenericAPIException(ServiceError.IMAGE_UPLOAD_FAILED);
        }
    }

    /**
     * Method to get the image from the system using image id
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#id")
    public ImageData getImageById(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var optionalImage = this.imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            return optionalImage.get();
        } else {
            log.error("[ImageStore - Get Image] - Image not found for the provided id({}).", id);
            throw new GenericAPIException(ServiceError.IMAGE_DATA_NOT_FOUND ,id);
        }
    }

    /**
     * Method to set the Image byte stream in ImageDataDTO and return it.
     *
     * @param imageData
     * @return
     */
    public ImageDataDTO getImageDTOForTheImageData(ImageData imageData) {
        try {
            var imageDTO = ImageDataMapper.INSTANCE.toDTO(imageData);
            var byteStream = ImageStoreUtil.readBytesFromFilePath(imageData.getFilePath());
            imageDTO.setImage(byteStream);
            return imageDTO;
        } catch (Exception e) {
            log.error("[ImageStore - Get Image] - Failed to get the Image(Id: {}) file from the system.", imageData.getId(), e);
            throw new GenericAPIException(ServiceError.FAILED_TO_LOAD_FILE, imageData.getId());
        }

    }
    /**
     * Method to load the image as byte stream from the system using image id
     *
     * @param id
     * @return
     */
    public byte[] loadImageById(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var optionalImage = this.imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            try {
                var imageData = optionalImage.get();
                return ImageStoreUtil.readBytesFromFilePath(imageData.getFilePath());
            } catch (Exception e) {
                log.error("[ImageStore - Load Image] - Failed to load the Image(Id: {}) from the system.", id, e);
                throw new GenericAPIException(ServiceError.FAILED_TO_LOAD_FILE, id);
            }
        } else {
            log.error("[ImageStore - Load Image] - Failed to load the Image(Id: {}) from the system.", id);
            throw new GenericAPIException(ServiceError.IMAGE_DATA_NOT_FOUND, id);
        }
    }

    /**
     * Method to get all uploaded images from the system
     *
     * @return
     * @throws GenericAPIException
     */
    public AggregatedImagesDTO getAllImages() throws GenericAPIException {
        var imageDataList = this.imageRepository.findAll();
        AggregatedImagesDTO aggregatedImagesDTO = new AggregatedImagesDTO();
        if (!imageDataList.isEmpty()) {
            var imageDataDTOList = ImageDataMapper.INSTANCE.toDTOList(imageDataList);
            imageDataDTOList.forEach(this::setImageBytesInDTO);
            aggregatedImagesDTO.setImages(imageDataDTOList);
        } else {
            return aggregatedImagesDTO;
        }
        return aggregatedImagesDTO;
    }

    private void setImageBytesInDTO(ImageDataDTO dto) {
        try {
            var byteStream = ImageStoreUtil.readBytesFromFilePath(dto.getFilePath());
            dto.setImage(byteStream);
        } catch (IOException e) {
            log.warn("[ImageStore - GetAllImages] - Failed to load the Image(Id: {}) from the system.",
                    dto.getId());
        }
    }

    /**
     * Method to delete an image by Id
     *
     * @param id
     */
    @CacheEvict(key = "#id")
    public void deleteImage(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var fileData = this.imageRepository.findById(id);
        if (fileData.isPresent()) {
            var file = fileData.get();
            var image = new File(file.getFilePath());
            if (image.delete()) {
                this.imageRepository.deleteById(id);
                log.info("[ImageStore - Delete Image] - Image was successfully deleted from the file system.");
            } else {
                throw new GenericAPIException(ServiceError.IMAGE_DELETE_FAILED);
            }
        } else {
            log.error("[ImageStore - Delete Image] - Image not found for the provided id({}).", id);
            throw new GenericAPIException(ServiceError.IMAGE_DATA_NOT_FOUND, id);
        }
    }

    /**
     * Method to update Image and Desc for an already existing ImageData
     *
     * @param image
     * @param desc
     * @param id
     * @return
     * @throws GenericAPIException
     */
    @CachePut(key = "#id")
    public ImageData updateImageById(MultipartFile image, String desc, Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        ImageStoreValidator.validateImage(image);
        validateImageSize(image.getSize());
        var optionalOldImage = this.imageRepository.findById(id);
        if (optionalOldImage.isPresent()) {
            var existingImageData = optionalOldImage.get();
            String oldImagePath = existingImageData.getFilePath();
            var oldImage = new File(oldImagePath);
            try {
                updateExistingImageWithNewImage(id, existingImageData, image);
                if (!desc.isBlank()) {
                    existingImageData.setDescription(desc);
                }
                var savedImageData = this.imageRepository.save(existingImageData);
                log.info("[ImageStore - Update Image] - ImageData was successfully updated in the DB.");
                return savedImageData;
            }
            catch(Exception ex) {
                rollbackImageUpdateInFileSystem(existingImageData.getFilePath(), oldImage, oldImagePath);
            }
        } else {
            log.error("[ImageStore - Update Image] - File not found for the given Id: {}.", id);
            throw new GenericAPIException(ServiceError.IMAGE_DATA_NOT_FOUND, id);
        }
        return null;
    }

    /**
     * Method to validate the Image size
     *
     * @param imageSize
     */
    private void validateImageSize(Long imageSize) {
        if (imageSize > Long.parseLong(maximumImageSize)) {
            throw new GenericAPIException(ServiceError.IMAGE_SIZE_LARGE, maximumImageSize);
        }
    }

    /**
     * Method to update the existing image with the provided image.
     *
     * @param id
     * @param existingImageData
     * @param newImage
     */
    private void updateExistingImageWithNewImage(Long id, ImageData existingImageData, MultipartFile newImage) {
        ImageStoreValidator.validateImage(newImage);
        validateImageSize(newImage.getSize());
        try {
            var oldImage = new File(existingImageData.getFilePath());
            if (oldImage.exists() && oldImage.delete()) {
                log.info("[ImageStore - Update Image] - Previous image was successfully deleted from the file system.");
                var newImagePath = ImageStoreUtil
                        .generateFilePath(basePath, newImage.getOriginalFilename());
                existingImageData.setFilePath(newImagePath);
                existingImageData.setName(ImageStoreUtil
                        .getFilenameWithoutExtension(newImage.getOriginalFilename()));
                newImage.transferTo(Path.of(newImagePath));
                log.info("[ImageStore - Update Image] - New image was successfully saved to the file system.");
            } else {
                log.error("[ImageStore - Update Image] - Failed to update the ImageData(Id: {}) with latest image as the previous file was not successfully deleted", id);
                throw new GenericAPIException(ServiceError.IMAGE_UPDATE_FAILED, existingImageData.getId());
            }
        } catch (IOException exception) {
            log.error("[ImageStore - Update Image] - Failed to update the ImageData(Id: {}) with latest image as the previous file was not successfully deleted", id, exception);
            throw new GenericAPIException(ServiceError.IMAGE_UPDATE_FAILED, existingImageData.getId());
        }
    }

    /**
     * Method to remove image if it got saved without updating the metadata in db and save back the old one
     *
     * @param newImagePath
     * @param oldImage
     * @param oldImagePath
     * @throws GenericAPIException
     */
    private void rollbackImageUpdateInFileSystem(String newImagePath, File oldImage, String oldImagePath)
            throws GenericAPIException {
        var newImage = new File(newImagePath);
        if (newImage.exists()) {
            newImage.delete();
        }
        var existingImage = new File(oldImagePath);
        if (!existingImage.exists()) {
            ImageStoreUtil.writeBytesToFile(oldImagePath, oldImage);
        }
    }

}
