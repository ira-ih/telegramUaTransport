package uatransport.telegrambot.service;

import uatransport.telegrambot.model.FeedbackModel;

public interface FeedbackModelService {

    public int getLastQuestionId(Long id);
    
    public void save(FeedbackModel feedbackModel);
}
