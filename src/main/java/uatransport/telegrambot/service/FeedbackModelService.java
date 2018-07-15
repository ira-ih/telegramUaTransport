package uatransport.telegrambot.service;

import uatransport.telegrambot.model.FeedbackModel;

public interface FeedbackModelService {

    int getLastQuestionId(Long id);

    void save(FeedbackModel feedbackModel);

    void updateTransportType(String type, long chatId);

    void updateTransportNumber(String number, long chatId);

    String currentTransportNumber(long chatId);

    String currentTransportType(long chatId);

    FeedbackModel getDistinctTopByChatIdOrderByDateDesc(long chatId);

    void saveFedbackModel(FeedbackModel feedbackModel);
}
