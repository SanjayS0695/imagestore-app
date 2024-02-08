package com.codecademy.imagestore.controller;

import com.codecademy.imagestore.auth.JwtUtil;
import com.codecademy.imagestore.dto.AggregatedImagesDTO;
import com.codecademy.imagestore.dto.ImageDataDTO;
import com.codecademy.imagestore.entity.ImageData;
import com.codecademy.imagestore.entity.UserData;
import com.codecademy.imagestore.enums.ImageType;
import com.codecademy.imagestore.enums.Role;
import com.codecademy.imagestore.repository.ImageRepository;
import com.codecademy.imagestore.repository.UserRepository;
import com.codecademy.imagestore.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.codecademy.imagestore.util.TestUtil.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost";

    private static TestRestTemplate restTemplate;
    private ImageData imageData;

    private HttpHeaders headers = null;
    private String url = null;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate(TestRestTemplate.HttpClientOption.values());
    }

    @BeforeEach
    public void setup() throws IOException {
        url = baseUrl + ":" + port + "/images";

        headers = new HttpHeaders();
        var mockUser = new UserData();
        mockUser.setFirstName("Test");
        mockUser.setPassword("password");
        mockUser.setLastName("User");
        mockUser.setEmail("testuser@imagestore.com");
        mockUser.setRole(Role.USER);

        userRepository.save(mockUser);
        var token = jwtUtil.generateToken(mockUser);
        headers.add("Authorization", "Bearer " + token);

        var testData = new ImageData();
        testData.setId(1L);
        testData.setName("TestImage");
        testData.setFilePath(TEST_IMAGE_PATH);
        testData.setDescription("Desc");
        testData.setType(ImageType.PNG);
        imageRepository.save(testData);
        TestUtil.loadImageFromTestData();
    }

    @AfterEach
    public void afterSetup() throws IOException {
        imageRepository.deleteAll();
        userRepository.deleteAll();
        var directory = new File(TEST_IMAGE_ROOT);
        FileUtils.cleanDirectory(directory);
    }

    @Test
    void uploadImageTestWithoutAuthentication() {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, new HttpHeaders());

        var response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }

    @Test
    void uploadImageTest() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image2",
                "image/jpeg", byteStream);
        LinkedMultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();

        headerMap.add("Content-disposition", "form-data;name=image; filename=" +image.getOriginalFilename());
        headerMap.add("Content-type", "image/png");

        HttpEntity<byte[]> imageFile = new HttpEntity<>(image.getBytes(),headerMap);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile);
        body.add("desc", "Description");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void uploadImageTestWhenContentTypeIsNotJPEGOrPNG() throws IOException {
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image2",
                "image/jpeg", byteStream);
        LinkedMultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();

        headerMap.add("Content-disposition", "form-data;name=image; filename=" +image.getOriginalFilename());
        headerMap.add("Content-type", "application/json");

        HttpEntity<byte[]> imageFile = new HttpEntity<>(image.getBytes(),headerMap);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile);
        body.add("desc", "Description");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        var response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        String errorMessage = response.getBody();
        Assertions.assertEquals("Uploaded image should only be of the format JPEG or PNG.", errorMessage);
    }

    @Test
    void getImageByIdWithoutAuthentication() {
        var getUrl = url + "/1";
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, new HttpHeaders());
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }

    @Test
    void getImageById() throws IOException {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        var getUrl = url + "/" + id;
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        ImageDataDTO imageDataDTO = mapper.readValue(response.getBody(), ImageDataDTO.class);
        Assertions.assertEquals(id, imageDataDTO.getId());
        Assertions.assertEquals("TestImage", imageDataDTO.getName());
        Assertions.assertEquals("Desc", imageDataDTO.getDescription());
        Assertions.assertEquals(ImageType.PNG, imageDataDTO.getType());
        Assertions.assertEquals("src/test/resources/imagestore/testimage.png", imageDataDTO.getFilePath());
        Assertions.assertNotNull(imageDataDTO.getImage());
    }

    @Test
    void getImageByIdWhenImageDataNotFound() {
        imageRepository.deleteAll();
        var getUrl = url + "/1";
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    void getAllImages() throws IOException {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        AggregatedImagesDTO aggregatedImagesDTO = mapper.readValue(response.getBody(), AggregatedImagesDTO.class);
        Assertions.assertEquals(1, aggregatedImagesDTO.getImages().size());
        Assertions.assertEquals("TestImage", aggregatedImagesDTO.getImages().get(0).getName());
        Assertions.assertEquals("Desc", aggregatedImagesDTO.getImages().get(0).getDescription());
        Assertions.assertEquals(ImageType.PNG, aggregatedImagesDTO.getImages().get(0).getType());
        Assertions.assertEquals("src/test/resources/imagestore/testimage.png", aggregatedImagesDTO.getImages().get(0).getFilePath());
        Assertions.assertNotNull(aggregatedImagesDTO.getImages().get(0).getImage());
    }

    @Test
    void getAllImagesWhenNotEntryInDB() throws IOException {
        imageRepository.deleteAll();
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        AggregatedImagesDTO aggregatedImagesDTO = mapper.readValue(response.getBody(), AggregatedImagesDTO.class);
        Assertions.assertEquals(0, aggregatedImagesDTO.getImages().size());
    }

    @Test
    void loadImageByIdWithoutAuthentication() {
        var getUrl = url + "/1/_load";
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, new HttpHeaders());
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }

    @Test
    void loadImageById() throws IOException {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        var getUrl = url + "/" + id + "/_load";
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void loadImageByIdWhenEntityNotFound() {
        imageRepository.deleteAll();
        var getUrl = url + "/1/_load";
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    void updateImageTestWithoutAuthentication() {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, new HttpHeaders());

        var response = restTemplate.exchange(url+ "/1", HttpMethod.PATCH, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }

    @Test
    void updateImageTest() throws IOException {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image2",
                "image/jpeg", byteStream);
        LinkedMultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();

        headerMap.add("Content-disposition", "form-data;name=image; filename=" +image.getOriginalFilename());
        headerMap.add("Content-type", "image/png");

        HttpEntity<byte[]> imageFile = new HttpEntity<>(image.getBytes(),headerMap);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile);
        body.add("desc", "Description");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(url+ "/" + id, HttpMethod.PATCH, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        String responseId = response.getBody();
        Assertions.assertEquals(id.toString(), responseId);
    }

    @Test
    void updateImageTestWhenContentTypeIsNotJPEGOrPNG() throws IOException {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image2",
                "image/jpeg", byteStream);
        LinkedMultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();

        headerMap.add("Content-disposition", "form-data;name=image; filename=" +image.getOriginalFilename());
        headerMap.add("Content-type", "application/json");

        HttpEntity<byte[]> imageFile = new HttpEntity<>(image.getBytes(),headerMap);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile);
        body.add("desc", "Description");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(url + "/" + id, HttpMethod.PATCH, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        String error = response.getBody();
        Assertions.assertEquals("Uploaded image should only be of the format JPEG or PNG.", error);
    }

    @Test
    void updateImageTestWhenNotJPEGOrPNG() throws IOException {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        var byteStream = Files.readAllBytes(new File(REF_IMAGE_PATH).toPath());
        MultipartFile image = new MockMultipartFile("image", "image2",
                "image/jpeg", byteStream);
        LinkedMultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();

        headerMap.add("Content-disposition", "form-data;name=image; filename=" +image.getOriginalFilename());
        headerMap.add("Content-type", "application/json");

        HttpEntity<byte[]> imageFile = new HttpEntity<>(image.getBytes(),headerMap);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile);
        body.add("desc", "Description");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(url + "/" + id, HttpMethod.PATCH, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        String errorResponse = response.getBody();
        Assertions.assertEquals("Uploaded image should only be of the format JPEG or PNG.", errorResponse);
    }

    @Test
    void deleteImageByIdWithoutAuthentication() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, new HttpHeaders());
        var response = restTemplate.exchange(url + "/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }

    @Test
    void deleteImageById() {
        Long id = TestUtil.getCurrentID(imageRepository.findAll());
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void deleteImageByIdWhenEntityNotFound() {
        imageRepository.deleteAll();
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        var response = restTemplate.exchange(url + "/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }
}
