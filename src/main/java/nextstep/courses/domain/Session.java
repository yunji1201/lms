package nextstep.courses.domain;

import nextstep.courses.constants.SessionStatus;

import java.time.LocalDate;

public class Session {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Image sessionImage;
    private boolean isFree;
    private int maxEnrollment;
    private int enrollCount;
    private int sessionFee;
    private SessionStatus status;

    public Session(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, boolean isFree,
                   int maxEnrollment, int enrollCount, int sessionFee, SessionStatus status) {
        validateDate(startDate, endDate);

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessionImage = sessionImage;
        this.isFree = isFree;
        this.maxEnrollment = maxEnrollment;
        this.enrollCount = enrollCount;
        this.sessionFee = sessionFee;
        this.status = status;
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

    public void enroll(int payment) {
        if (status != SessionStatus.OPEN) {
            throw new IllegalStateException("수강 신청은 모집중인 상태에서만 가능합니다.");
        }
        if (isFree) {
            enrollCount++;
            return;
        }
        if (enrollCount >= maxEnrollment) {
            throw new IllegalStateException("수강 인원이 초과되었습니다.");
        }
        if (payment != sessionFee) {
            throw new IllegalArgumentException("결제 금액이 수강료와 일치하지 않습니다.");
        }
        enrollCount++;
    }

    public int getEnrolledCount() {
        return enrollCount;
    }

}
