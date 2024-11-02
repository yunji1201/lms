package nextstep.courses.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {

    @Test
    @DisplayName("Course 생성 가능한지 확인")
    void createCourse() {
        Course course = new Course("TDD, 클린 코드 with Java", 1L);

        assertThat(course.getTitle()).isEqualTo(course.getTitle());
        assertThat(course.getCreatorId()).isEqualTo(course.getCreatorId());
        assertThat(course.getCreatedAt()).isNotNull();
    }

}
