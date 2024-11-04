package nextstep.qna.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private final List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void markAllDeleted(NsUser writer) {
        answers.stream()
                .filter(answer -> answer.isOwner(writer))
                .forEach(Answer::markAsDeleted);
    }


    public boolean isAllOwnedBy(NsUser writer) {
        return answers.stream().allMatch(answer -> answer.isOwner(writer));
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }

    public List<Answer> toList() {
        return answers;
    }

}
