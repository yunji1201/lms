package nextstep.courses.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.courses.domain.SessionRepository;
import nextstep.courses.domain.session.FreeSession;
import nextstep.courses.domain.session.PaidSession;
import nextstep.courses.domain.session.Session;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

@JdbcTest
public class SessionRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRepositoryTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM session");
        sessionRepository = new JdbcSessionRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("유료 강의 생성 / 조회")
    void saveAndFindPaidSessionById() {
        Session paidSession = new PaidSession(1L, "유료 강의", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null, 30, 50000);

        sessionRepository.save(paidSession);
        Session savedSession = sessionRepository.findById(paidSession.getId());

        assertThat(savedSession).isInstanceOf(PaidSession.class);
        assertThat(paidSession.getTitle()).isEqualTo(savedSession.getTitle());
        LOGGER.debug("Saved Paid Session: {}", savedSession);
    }

    @Test
    @DisplayName("무료 강의 생성 / 조회")
    void saveAndFindFreeSessionById() {
        Session freeSession = new FreeSession(1L, 1L, "무료 강의", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null);

        sessionRepository.save(freeSession);
        Session savedSession = sessionRepository.findById(freeSession.getId());

        assertThat(savedSession).isInstanceOf(FreeSession.class);
        assertThat(savedSession.getTitle()).isEqualTo(freeSession.getTitle());
        LOGGER.debug("Saved Free Session: {}", savedSession);
    }

    @Test
    @DisplayName("강의 정보 변경")
    void updateSession() {
        Session paidSession = new PaidSession(1L, "유료 강의", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null, 30, 50000);
        sessionRepository.save(paidSession);

        paidSession.setTitle("유료 강의 (수정)");
        int count = sessionRepository.update(paidSession);
        assertThat(count).isEqualTo(1);

        Session updatedSession = sessionRepository.findById(paidSession.getId());
        assertThat(updatedSession).isInstanceOf(PaidSession.class);
        assertThat(updatedSession.getTitle()).isEqualTo("유료 강의 (수정)");
        LOGGER.debug("Updated Session: {}", updatedSession);
    }

    @Test
    @DisplayName("강의 삭제")
    void deleteSessionById() {
        Session freeSession = new FreeSession(1L, 1L, "무료 강의", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null);
        sessionRepository.save(freeSession);

        int count = sessionRepository.deleteById(freeSession.getId());
        assertThat(count).isEqualTo(1);

        Session deletedSession = sessionRepository.findById(freeSession.getId());
        assertThat(deletedSession).isNull();
        LOGGER.debug("Deleted Session: {}", deletedSession);
    }
}
