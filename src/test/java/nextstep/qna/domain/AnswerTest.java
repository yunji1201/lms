package nextstep.qna.domain;

import nextstep.qna.exception.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(NsUserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("로그인 사용자와 답변자가 같은 경우 답변 삭제 가능한지 확인")
    void loginUserEqualsAnswerUser() throws CannotDeleteException {
        A1.validationOwner(NsUserTest.JAVAJIGI);
        A1.markAsDeleted();
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 답변자가 다른 경우 에러 발생하는지 확인")
    void loginUserDifferentAnswerUser() {
        assertThatThrownBy(() -> {
            A1.validationOwner(NsUserTest.SANJIGI);
            A1.markAsDeleted();
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변이 삭제되었을 시 히스토리가 남겨지는지 확인")
    void createDeleteAnswerHistory() throws CannotDeleteException {
        A1.validationOwner(NsUserTest.JAVAJIGI);
        A1.markAsDeleted();

        LocalDateTime createdDate = LocalDateTime.now();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), createdDate);

        DeleteHistory expectedHistory = new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), createdDate);

        assertThat(deleteHistory).isEqualTo(expectedHistory);
    }

}
