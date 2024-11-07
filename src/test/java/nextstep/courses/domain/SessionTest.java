package nextstep.courses.domain;

import nextstep.courses.domain.session.FreeSession;
import nextstep.courses.domain.session.PaidSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    private Image image;
    private FreeSession freeSession;
    private PaidSession paidSession;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        this.image = new Image("강의이미지", 1, "jpg", 300, 200);
        this.freeSession = new FreeSession(1L, "무료 강의", today, today.plusDays(10), image);
        this.paidSession = new PaidSession(1L, "유료 강의", today, today.plusDays(10), image, 10, 100000);
        this.freeSession.startEnrollment();
        this.paidSession.startEnrollment();
    }

    @Test
    @DisplayName("강의 생성 가능한지 확인")
    void createSession() {
        FreeSession session = new FreeSession(1L, "무료 강의", today, today.plusDays(10), image);

        assertThat(session)
                .extracting("title", "startDate", "endDate", "sessionImage")
                .containsExactly("무료 강의", today, today.plusDays(10), image);
    }

    @Test
    @DisplayName("무료 강의는 최대 수강 인원 제한 없음 확인")
    void enrollFreeSession() {
        IntStream.range(0, 1000).forEach(i -> freeSession.enroll(0));
        assertThat(freeSession.getEnrolledCount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("유료 강의 신청 시 결제 금액이 수강료와 다를 경우 예외 발생")
    void enrollIncorrectPaymentThrowException() {
        assertThatThrownBy(() -> paidSession.enroll(90000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유료 강의 신청 시 결제 금액이 수강료와 일치하면 수강 신청 성공")
    void enrollCorrectPaymentThrowException() {
        paidSession.enroll(100000);
        assertThat(paidSession.getEnrolledCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("유료 강의는 최대 수강 인원 초과 시 예외 발생")
    void overMaxEnrollmentThrowsException() {
        IntStream.range(0, 10).forEach(i -> paidSession.enroll(100000));
        assertThatThrownBy(() -> paidSession.enroll(100000))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("모집 중이 아닌 강의 수강 신청 시 예외 발생")
    void enrollReadySessionThrowsException() {
        FreeSession readySession = new FreeSession(1L, "무료 강의", today, today.plusDays(10), image);
        assertThatThrownBy(() -> readySession.enroll(0)).isInstanceOf(IllegalStateException.class);
    }
}
