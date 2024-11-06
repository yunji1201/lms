package nextstep.courses.domain.session;

import nextstep.courses.constants.SessionStatus;
import nextstep.courses.domain.Image;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Session {
    protected Long id;
    protected Long courseId;
    protected String title;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected Image sessionImage;
    protected int enrollCount;
    protected SessionStatus status;

    public Session(Long id, Long courseId, String title, LocalDate startDate, LocalDate endDate, Image sessionImage, SessionStatus status) {
        validateDate(startDate, endDate);
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessionImage = sessionImage;
        this.status = status;
        this.enrollCount = 0;
    }


    public Session(String title, LocalDate startDate, LocalDate endDate, Image sessionImage, SessionStatus status) {
        this(null, null, title, startDate, endDate, sessionImage, status);
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

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public LocalDate getStartTime() {
        return this.startDate;
    }

    public LocalDate getEndTime() {
        return this.endDate;
    }

    public void setId(long id) {
        this.id = id;
    }
}
