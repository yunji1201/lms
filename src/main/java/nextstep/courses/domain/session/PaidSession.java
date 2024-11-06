package nextstep.courses.domain.session;

import nextstep.courses.constants.SessionStatus;
import nextstep.courses.domain.Image;

import java.time.LocalDate;

public class PaidSession extends Session {
    private int maxEnrollment;
    private int sessionFee;

    public abstract class Session {
        protected Long courseId;

        public Session(Long id, Long courseId, String title, LocalDate startDate, LocalDate endDate, Image sessionImage, SessionStatus status) {
            this.courseId = courseId;
        }
    }

    public PaidSession(Long courseId, String title, LocalDate startDate, LocalDate endDate, Image sessionImage, int maxEnrollment, int sessionFee) {
        super(null, courseId, title, startDate, endDate, sessionImage, SessionStatus.READY);
        this.maxEnrollment = maxEnrollment;
        this.sessionFee = sessionFee;
    }

    @Override
    public void enroll(int payment) {
        if (status != SessionStatus.OPEN) {
            throw new IllegalStateException("수강 신청은 모집중인 상태에서만 가능합니다.");
        }
        if (enrollCount >= maxEnrollment) {
            throw new IllegalStateException("수강 인원이 초과되었습니다.");
        }
        if (payment != sessionFee) {
            throw new IllegalArgumentException("결제 금액이 수강료와 일치하지 않습니다.");
        }
        enrollCount++;
    }

    public int getSessionFee() {
        return sessionFee;
    }
}
