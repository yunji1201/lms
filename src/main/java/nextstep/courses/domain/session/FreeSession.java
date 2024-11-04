package nextstep.courses.domain.session;

import nextstep.courses.constants.SessionStatus;
import nextstep.courses.domain.Image;

import java.time.LocalDate;

public class FreeSession extends Session {
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
