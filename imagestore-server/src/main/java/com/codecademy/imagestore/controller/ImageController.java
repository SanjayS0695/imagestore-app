package com.codecademy.imagestore.controller;

import com.codecademy.imagestore.dto.AggregatedImagesDTO;
import com.codecademy.imagestore.dto.ImageDataDTO;
import com.codecademy.imagestore.entity.ImageData;
import com.codecademy.imagestore.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Images", description = "Images management APIs")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;


    @Operation(
            summary = "Upload Image to the ImageStore system.",
            description = "Upload Image to the system. The Image will be stored in the file system and " +
                    "the related ImageData with metadata of the image will be persisted in DB.",
            tags = {"post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Long> uploadImage(final @RequestParam(value = "image") MultipartFile multipartFile, final @RequestParam(value = "desc", required = false) String description) {
        return ResponseEntity.ok(this.imageService.uploadImage(multipartFile, description));
    }

    @Operation(
            summary = "Get Image from the ImageStore by Id.",
            description = "Get Image from the system. The Image will be fetched from the system by the given Id.",
            tags = {"get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ImageData.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDataDTO> getImageById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(this.imageService.getImageById(id));
    }

    @Operation(
            summary = "Load Image from the ImageStore by Id.",
            description = "Load Image from the system. The Image will be fetched from the system as byte streams.",
            tags = {"get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = byte.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @GetMapping(value = "/{id}/_load", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> loadImageById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(this.imageService.loadImageById(id));
    }

    @Operation(
            summary = "Get all Images from the ImageStore.",
            description = "Get all Images from the system. The response will be sent as AggregatedImagesDTO",
            tags = {"get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AggregatedImagesDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AggregatedImagesDTO> fetchAllImages() {
        return ResponseEntity.ok(this.imageService.getAllImages());
    }

    @Operation(
            summary = "Update Image in ImageStore.",
            description = "Get Images in the system by the given Id with new Image and description",
            tags = {"patch"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateImageById(final @RequestParam(value = "image") MultipartFile image,
                                                final @RequestParam(value = "desc", required = false) String desc,
                                                @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(this.imageService.updateImageById(image, desc, id));
    }

    @Operation(
            summary = "Delete Image in ImageStore.",
            description = "Delete Images in the system by the given Id.",
            tags = {"delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteImageById(@PathVariable(value = "id") Long id) {
        this.imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
}
