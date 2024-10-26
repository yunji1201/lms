package nextstep.qna.domain;

import nextstep.qna.exception.CannotDeleteException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public boolean markDeleted() {
        return this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isOwner(NsUser loginUser) {
        return writer.equals(loginUser);
    }

    public boolean hasNoAnswers() {
        return answers.isEmpty();
    }

    public List<DeleteHistory> delete(NsUser loginUser) throws CannotDeleteException {

        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if (!hasNoAnswers()) {
            checkAllAnswersOwnedBy(loginUser);
        }
        markDeleted();

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(createDeleteHistory());
        deleteHistories.addAll(answers.deleteAllOwnedBy(loginUser));

        return deleteHistories;
    }

    public DeleteHistory createDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now());
    }

    public void checkAllAnswersOwnedBy(NsUser loginUser) throws CannotDeleteException {
        if (!answers.isAllOwnedBy(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public long getId() {
        return this.id = id;
    }

    public NsUser getWriter() {
        return this.writer = writer;
    }

    public Object getAnswers() {
        return this.answers = answers;
    }
}
