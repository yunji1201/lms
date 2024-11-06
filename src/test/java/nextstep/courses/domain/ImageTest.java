package nextstep.courses.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageTest {

    @Test
    @DisplayName("이미지 생성 가능한지 확인")
    void createImage() {
        Image image = new Image(1, "강의_이미지", 1, "jpg", 300, 200);
        assertThat(image)
                .extracting("fileName", "fileSize", "fileType", "width", "height")
                .containsExactly("강의_이미지", 1, "jpg", 300, 200);
    }

    @Test
    @DisplayName("이미지 크기 1MB 초과하면 예외 발생")
    void createOverSizeImageThrowException() {
        assertThatThrownBy(() -> new Image(1, "강의_이미지", 2, "jpg", 300, 200))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미지 너비 300px, 높이 200px 미만이면 예외 발생")
    void createOverStandardImageThrowException() {
        assertThatThrownBy(() -> new Image(1, "강의_이미지", 1, "jpg", 200, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미지 비율이 3:2가 아닐 때 예외 발생")
    void createImageRateThrowExceptionTest() {
        assertThatThrownBy(() -> new Image(1, "강의_이미지", 1, "jpg", 300, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
