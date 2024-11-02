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

    public Session() {
    }

    public Session(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, boolean isFree, int maxEnrollment, int sessionFee) {
        this(title, startDate, endDate, sessionImage, isFree, maxEnrollment, 0, sessionFee, SessionStatus.READY);
    }

    public Session(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, boolean isFree,
                   int maxEnrollment, int enrollCount, int sessionFee, SessionStatus status) {
        validationDate(startDate, endDate);

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

    private void validationDate(LocalDate startDate, LocalDate endDate) {
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

    public void enroll() {
        enroll(0);
    }

    public void enroll(int payment) {
        enrollCount++;
    }

    public int getEnrolledCount() {
        return enrollCount;
    }

}
