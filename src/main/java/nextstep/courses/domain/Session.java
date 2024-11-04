package nextstep.courses.domain;

import nextstep.courses.constants.SessionStatus;

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

    protected void validateDate(LocalDate startDate, LocalDate endDate) {
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

class FreeSession extends Session {
    public FreeSession(String title, LocalDate startDate, LocalDate endDate, Image sessionImage) {
        super(title, startDate, endDate, sessionImage, SessionStatus.READY);
    }

    @Override
    public void enroll(int payment) {
        if (status != SessionStatus.OPEN) {
            throw new IllegalStateException("수강 신청은 모집중인 상태에서만 가능합니다.");
        }
        enrollCount++;
    }
}

class PaidSession extends Session {
    private int maxEnrollment;
    private int sessionFee;

    public PaidSession(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, int maxEnrollment, int sessionFee) {
        super(title, startDate, endDate, sessionImage, SessionStatus.READY);
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
}
