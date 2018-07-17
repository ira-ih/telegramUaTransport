package uatransport.telegrambot.service;

import uatransport.telegrambot.entity.FeedbackModel;
import uatransport.telegrambot.entity.Question;

import java.util.List;

public interface FeedbackModelService {

    Question getLastQuestion(Long id);

    void save(FeedbackModel feedbackModel);

    void updateTransitId(Integer transitId, long chatId);

    void updateQuestion(Question question, long chatId);

    Integer currentTransitId(long chatId);

    String currentUuid(long chatId);

    FeedbackModel getDistinctTopByChatIdOrderByDateDesc(long chatId);

    void updateAnswer(String answer, long chatId);

    void saveFedbackModel(FeedbackModel feedbackModel);

    List<FeedbackModel> findAllByChatId(Long chatId);
    void deleteAllByChatId(Long chatId);

}
