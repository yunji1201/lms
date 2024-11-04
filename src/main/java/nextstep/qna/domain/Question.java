package nextstep.qna.domain;

import nextstep.qna.exception.CannotDeleteException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class Question {
    private Long id;

    private String title;

    private String contents;

    private NsUser writer;

    private Answers answers = new Answers();

    private boolean deleted = false;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;

    public Question() {
    }

    public Question(NsUser writer, String title, String contents) {
        this(0L, writer, title, contents);
    }

    public Question(Long id, NsUser writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public long getId() {
        return this.id;
    }

    public NsUser getWriter() {
        return this.writer;
    }

    public Answers getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    private boolean markDeleted() {
        return this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private boolean isOwner(NsUser loginUser) {
        return writer.equals(loginUser);
    }

    private boolean hasAnswers() {
        return !answers.isEmpty();
    }

    public void delete(NsUser loginUser) throws CannotDeleteException {

        validationOwner(loginUser);

        if (hasAnswers()) {
            checkAllAnswersOwnedBy(loginUser);
        }

        markDeleted();
        answers.markAllDeleted(loginUser);
    }

    private void validationOwner(NsUser loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void checkAllAnswersOwnedBy(NsUser loginUser) throws CannotDeleteException {
        if (!answers.isAllOwnedBy(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

}
