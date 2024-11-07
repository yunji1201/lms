package nextstep.courses.infrastructure;

import nextstep.courses.domain.Image;
import nextstep.courses.domain.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ImageRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageRepositoryTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM image");
        imageRepository = new JdbcImageRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("이미지 생성 / 조회")
    void saveAndFindById() {
        Image image = new Image("강의_이미지", 1, "jpg", 300, 200);

        imageRepository.save(image);
        Image savedImage = imageRepository.findById(image.getId());

        assertThat(savedImage.getFileName()).isEqualTo(image.getFileName());
        LOGGER.debug("Saved Image: {}", savedImage);
    }

    @Test
    @DisplayName("이미지 변경")
    void update() {
        Image image = new Image("강의_이미지", 1, "jpg", 300, 200);
        imageRepository.save(image);

        image.setFileName("강의_이미지_수정");
        int count = imageRepository.update(image);
        assertThat(count).isEqualTo(1);

        Image updatedImage = imageRepository.findById(image.getId());
        assertThat(updatedImage.getFileName()).isEqualTo("강의_이미지_수정");
        LOGGER.debug("Updated Image: {}", updatedImage);
    }

    @Test
    @DisplayName("이미지 삭제")
    void deleteById() {
        Image image = new Image("강의_이미지", 1, "jpg", 300, 200);
        imageRepository.save(image);

        int count = imageRepository.deleteById(image.getId());
        assertThat(count).isEqualTo(1);

        Image deletedImage = imageRepository.findById(image.getId());
        assertThat(deletedImage).isNull();
        LOGGER.debug("Deleted Image: {}", deletedImage);
    }

}
