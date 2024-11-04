package nextstep.courses.domain.session;

import nextstep.courses.constants.SessionStatus;
import nextstep.courses.domain.Image;

import java.time.LocalDate;

public abstract class Session {
    protected String title;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected Image sessionImage;
    protected int enrollCount;
    protected SessionStatus status;

    public Session(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, SessionStatus status) {
        validateDate(startDate, endDate);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessionImage = sessionImage;
        this.status = status;
        this.enrollCount = 0;
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일 이전이어야 합니다.");
        }
    }

    public void startEnrollment() {
        this.status = SessionStatus.OPEN;
    }

    public void closeEnrollment() {
        this.status = SessionStatus.CLOSED;
    }

    public abstract void enroll(int payment);

    public int getEnrolledCount() {
        return enrollCount;
    }
}

