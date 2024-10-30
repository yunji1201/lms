package nextstep.qna.service;

import nextstep.qna.domain.Answer;
import nextstep.qna.domain.ContentType;
import nextstep.qna.domain.DeleteHistory;
import nextstep.qna.domain.DeleteHistoryRepository;
import nextstep.qna.domain.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service("deleteHistoryService")
public class DeleteHistoryService {
    @Resource(name = "deleteHistoryRepository")
    private DeleteHistoryRepository deleteHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(List<DeleteHistory> deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories);
    }

    public DeleteHistory createDeleteHistoryForQuestion(Question question, LocalDateTime createdDate) {
        return new DeleteHistory(
                ContentType.QUESTION,
                question.getId(),
                question.getWriter(),
                createdDate
        );
    }

    public DeleteHistory createDeleteHistoryForAnswer(Answer answer, LocalDateTime createdDate) {
        return new DeleteHistory(
                ContentType.ANSWER,
                answer.getId(),
                answer.getWriter(),
                createdDate
        );
    }

}
