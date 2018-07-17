package uatransport.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uatransport.telegrambot.entity.FeedbackModel;
import uatransport.telegrambot.entity.Question;
import uatransport.telegrambot.repository.FeedbackModelRepository;
import uatransport.telegrambot.service.ChatModelService;
import uatransport.telegrambot.service.FeedbackModelService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FeedbackModelServiceImplementation implements FeedbackModelService {

    @Autowired
    private FeedbackModelRepository feedbackModelRepository;


    @Autowired
    private ChatModelService chatModelService;

    @Override
    public Question getLastQuestion(Long id) {

     return feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(id).getQuestion();
    }

    @Override
    public void save(FeedbackModel feedbackModel) {
        feedbackModelRepository.save(feedbackModel);

    }

    @Override
    @Transactional
    public void updateTransitId(Integer transitId, long chatId) {
       FeedbackModel feedbackModel= feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId);
        feedbackModel.setTransitId(transitId);
        feedbackModelRepository.save(feedbackModel);

    }

    @Override
    @Transactional
    public void updateQuestion(Question question, long chatId) {
       FeedbackModel feedbackModel= feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId);
     feedbackModel.setQuestion(question);
     feedbackModelRepository.save(feedbackModel);
    }

    @Override
    public Integer currentTransitId(long chatId) {
        return feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId).getTransitId();
    }

    @Override
    public String currentUuid(long chatId) {
      return   feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId).getUuid();
    }


    @Override
    public FeedbackModel getDistinctTopByChatIdOrderByDateDesc(long chatId) {
        return feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId);
    }

    @Override
    @Transactional
    public void updateAnswer(String answer, long chatId) {
       FeedbackModel feedbackModel =  feedbackModelRepository.getDistinctTopByChatIdOrderByDateDesc(chatId);
        feedbackModel.setAnswer(answer);
        feedbackModelRepository.save(feedbackModel);
    }

    @Override
    @Transactional
    public void saveFedbackModel(FeedbackModel feedbackModel) {
        feedbackModelRepository.save(feedbackModel);
    }

    @Override
    @Transactional
    public List<FeedbackModel> findAllByChatId(Long chatId) {
        return feedbackModelRepository.findAllByChatId(chatId);
    }

    @Override
    @Transactional
    public void deleteAllByChatId(Long chatId) {
        feedbackModelRepository.deleteAllByChatId(chatId);
    }

}
