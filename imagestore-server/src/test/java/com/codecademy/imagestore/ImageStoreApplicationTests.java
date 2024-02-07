package com.codecademy.imagestore;

import com.codecademy.imagestore.controller.ImageController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest
class ImageStoreApplicationTests {

	@Autowired
	ImageController imageController;

	@Test
	public void contextLoads() {
		Assertions.assertThat(imageController).isNotNull();
	}

}
