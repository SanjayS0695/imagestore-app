package com.codecademy.imagestore.repository;


import com.codecademy.imagestore.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, Long> {
}