package com.codecademy.imagestore.entity;

import com.codecademy.imagestore.enums.ImageType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Table(name = "images")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @Column
    private String description;

    @Enumerated
    @Column(nullable = false)
    private ImageType type;

    @Column(nullable = false, unique = true)
    private String filePath;

    @CreatedDate
    @Column
    private LocalDateTime createdOn;

    @CreatedBy
    @Column
    private String createdBy;

    @Column
    private LocalDateTime updatedOn;

    @Column
    private String updatedBy;
}
