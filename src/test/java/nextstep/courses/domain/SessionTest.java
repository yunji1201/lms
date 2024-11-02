package nextstep.courses.domain;

import nextstep.courses.constants.SessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    private Image image;
    private Session freeSession;
    private Session paidSession;

    @BeforeEach
    void setUp() {
        this.image = new Image("강의이미지", 1, "jpg", 300, 200);
        this.freeSession = new Session("무료 강의", LocalDate.now(), LocalDate.now().plusDays(10), image, true, 0, 0);
        this.paidSession = new Session("유료 강의", LocalDate.now(), LocalDate.now().plusDays(10), image, false, 10, 100000);
        this.freeSession.startEnrollment();
        this.paidSession.startEnrollment();

    }

    @Test
    @DisplayName("강의 생성 가능한지 확인")
    void createSession() {
        Session session = new Session("Spring Session", LocalDate.now(), LocalDate.now().plusDays(10), image, true, 0, 0);

        assertThat(session)
                .extracting("title", "startDate", "endDate", "sessionImage", "isFree", "maxEnrollment", "status", "sessionFee")
                .containsExactly("Spring Session", LocalDate.now(), LocalDate.now().plusDays(10), image, true, 0, SessionStatus.READY, 0);
    }

    @Test
    @DisplayName("무료 강의는 최대 수강 인원 제한 없음 확인")
    void enrollFreeSession() {
        IntStream.range(0, 1000).forEach(i -> freeSession.enroll());
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
        Session readySession = new Session("무료 강의", LocalDate.now(), LocalDate.now().plusDays(10), image, true, 0, 0);
        assertThatThrownBy(() -> readySession.enroll()).isInstanceOf(IllegalStateException.class);
    }
}
