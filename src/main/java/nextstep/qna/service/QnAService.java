package nextstep.qna.service;

import nextstep.qna.exception.CannotDeleteException;
import nextstep.qna.exception.NotFoundException;
import nextstep.qna.domain.*;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("qnaService")
public class QnAService {
    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    @Transactional
    public void deleteQuestion(NsUser loginUser, long questionId) throws CannotDeleteException {
        Question question = questionRepository.findById(questionId).orElseThrow(NotFoundException::new);
        question.delete(loginUser);

        LocalDateTime deletionTime = LocalDateTime.now();

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteHistoryService.createDeleteHistoryForQuestion(question, deletionTime));

        question.getAnswers().toList().stream()
                .filter(Answer::isDeleted)
                .map(answer -> deleteHistoryService.createDeleteHistoryForAnswer(answer, deletionTime))
                .forEach(deleteHistories::add);

        deleteHistoryService.saveAll(deleteHistories);
    }

}
