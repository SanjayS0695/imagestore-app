package com.codecademy.imagestore.service;

import com.codecademy.imagestore.entity.ImageData;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.repository.ImageRepository;
import com.codecademy.imagestore.util.TestUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codecademy.imagestore.util.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Mock
    ImageRepository imageRepository;

    @BeforeEach
    public void setup() throws IOException {
        ReflectionTestUtils.setField(imageService, "basePath", TEST_IMAGE_ROOT);
        ReflectionTestUtils.setField(imageService, "maximumImageSize", "10485767");
        TestUtil.loadImageFromTestData();
    }

    @AfterEach
    public void tearDown() throws IOException {
        var directory = new File(TEST_IMAGE_ROOT);
        FileUtils.cleanDirectory(directory);
    }

    @Test
    public void uploadImageTest() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image1",
                "image/jpeg", byteStream);
        var testData = new ImageData();
        testData.setId(1L);
        when(imageRepository.save(any(ImageData.class))).thenReturn(testData);
        imageService.uploadImage(image, "desc");
        ArgumentCaptor<ImageData> dataArgumentCaptor = ArgumentCaptor.forClass(ImageData.class);
        verify(imageRepository, times(1)).save(dataArgumentCaptor.capture());
        ImageData imageData = dataArgumentCaptor.getValue();
        Assertions.assertEquals("image1", imageData.getName());
        Assertions.assertEquals("desc", imageData.getDescription());
        Assertions.assertTrue(imageData.getFilePath().contains(TEST_IMAGE_ROOT));
        List<String> filesInDestination = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(TEST_IMAGE_ROOT))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        filesInDestination.add(String.valueOf(file.getFileName()));
                    });
        }
        var fileName = filesInDestination.stream().filter(s -> s.contains("image1")).findFirst();
        Assertions.assertTrue(fileName.isPresent());
    }

    @Test
    public void uploadImageTestWhenImageNull() {
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.uploadImage(null, "desc"));
    }

    @Test
    public void uploadImageTestWhenImageSizeGreaterThanMax() throws IOException {
        ReflectionTestUtils.setField(imageService, "maximumImageSize", "107");
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image1",
                "image/jpeg", byteStream);
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.uploadImage(image, "desc"));
    }

    @Test
    public void uploadImageTestWhenContentTypeIsNull() {
        MultipartFile image = new MockMultipartFile("image", "image1",
                null, new byte[]{});
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.uploadImage(image, "desc"));
    }

    @Test
    public void getImageById() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        var imageDataDTO = imageService.getImageById(1L);
        Assertions.assertEquals(1, imageDataDTO.getId());
        Assertions.assertNotNull(imageDataDTO.getImage());
    }

    @Test
    public void getImageByIdWhenImageDataNotFoundInDB() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath(TEST_IMAGE_PATH);
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.getImageById(1L));
    }

    @Test
    public void getImageByIdWhenImageNotFoundInFileSystem() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath("src/test/resources/imagestore/imageNotFound.png");
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.getImageById(1L));
    }

    @Test
    public void loadImageById() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        var byteStream = imageService.loadImageById(1L);
        Assertions.assertNotNull(byteStream);
    }

    @Test
    public void loadImageByIdWhenImageDataNotFoundInDB() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath(TEST_IMAGE_PATH);
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.loadImageById(1L));
    }

    @Test
    public void loadImageByIdWhenImageNotFoundInFileSystem() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath("src/test/resources/imagestore/imageNotFound.png");
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.loadImageById(1L));
    }

    @Test
    public void getAllImageById() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setName("image1");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findAll()).thenReturn(List.of(testData));
        var aggregatedImagesDTO = imageService.getAllImages();
        Assertions.assertNotNull(aggregatedImagesDTO);
        Assertions.assertEquals(1, aggregatedImagesDTO.getImages().size());
        Assertions.assertEquals("image1", aggregatedImagesDTO.getImages().get(0).getName());
        Assertions.assertNotNull(aggregatedImagesDTO.getImages().get(0).getImage());
    }

    @Test
    public void getAllImageByIdWhenNoImageData() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setName("image1");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findAll()).thenReturn(Collections.emptyList());
        var aggregatedImagesDTO = imageService.getAllImages();
        Assertions.assertNotNull(aggregatedImagesDTO);
        Assertions.assertEquals(0, aggregatedImagesDTO.getImages().size());
    }

    @Test
    public void deleteImageById() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        imageService.deleteImage(1L);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(imageRepository, times(1)).deleteById(idCaptor.capture());
        Long id = idCaptor.getValue();
        Assertions.assertEquals(1, id);
    }

    @Test
    public void deleteImageByIdWhenIdNull() {
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.deleteImage(null));
    }

    @Test
    public void updateImageTest() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image1",
                "image/jpeg", byteStream);
        var testData = new ImageData();
        testData.setId(1L);
        testData.setDescription("desc");
        testData.setName("testImage");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.save(any(ImageData.class))).thenReturn(testData);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        imageService.updateImageById(image, "newDesc", 1L);
        ArgumentCaptor<ImageData> dataArgumentCaptor = ArgumentCaptor.forClass(ImageData.class);
        verify(imageRepository, times(1)).save(dataArgumentCaptor.capture());
        ImageData imageData = dataArgumentCaptor.getValue();
        Assertions.assertEquals("image1", imageData.getName());
        Assertions.assertEquals("newDesc", imageData.getDescription());
        Assertions.assertTrue(imageData.getFilePath().contains(TEST_IMAGE_ROOT));
        Assertions.assertTrue(imageData.getFilePath().contains("image1"));
        List<String> filesInDestination = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(TEST_IMAGE_ROOT))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        filesInDestination.add(String.valueOf(file.getFileName()));
                    });
        }
        var fileName = filesInDestination.stream().filter(s -> s.contains("image1")).findFirst();
        Assertions.assertTrue(fileName.isPresent());
    }

    @Test
    public void updateImageWhenIdIsNullTest() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image1",
                "image/jpeg", byteStream);
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.updateImageById(image, "desc", null));
    }

    @Test
    public void updateImageTestWhenImageSizeGreaterThanMax() throws IOException {
        ReflectionTestUtils.setField(imageService, "maximumImageSize", "107");
        var testData = new ImageData();
        testData.setId(1L);
        testData.setDescription("desc");
        testData.setName("testImage");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image1",
                "image/jpeg", byteStream);
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.updateImageById(image, "desc", 1L));
    }

    @Test
    public void updateImageTestWhenContentTypeIsNull() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setDescription("desc");
        testData.setName("testImage");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        MultipartFile image = new MockMultipartFile("image", "image1",
                null, new byte[]{});
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.updateImageById(image, "desc", 1L));
    }

    @Test
    public void updateImageTestWhenContentTypeIsNotJPEGOrPng() {
        var testData = new ImageData();
        testData.setId(1L);
        testData.setDescription("desc");
        testData.setName("testImage");
        testData.setFilePath(TEST_IMAGE_PATH);
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testData));
        MultipartFile image = new MockMultipartFile("image", "image1",
                "application/json", new byte[]{});
        Assertions.assertThrows(GenericAPIException.class,
                () -> imageService.updateImageById(image, "desc", 1L));
    }
}
