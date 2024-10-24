package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @Test
    @DisplayName("로그인 사용자와 질문자가 같은 경우 질문 삭제 가능한지 확인")
    void loginUserEqualsQuestionUser() throws CannotDeleteException {
        Q1.delete(NsUserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 질문자가 다른 경우 에러 발생하는지 확인")
    void loginUserDifferentQuestionUser() {
        assertThatThrownBy(() -> {
            Q1.delete(NsUserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제 시 답변도 삭제되는지 확인")
    void deleteQuestionAndAnswers() throws CannotDeleteException {
        Answer answer = new Answer(NsUserTest.JAVAJIGI, Q1, "contents1");
        Q1.addAnswer(answer);

        Q1.delete(NsUserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 삭제 안되는지 확인")
    void loginUserDifferentFromAnswerOwner() {
        Answer answer = new Answer(NsUserTest.SANJIGI, Q1, "contents1");
        Q1.addAnswer(answer);

        assertThatThrownBy(() -> Q1.delete(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
