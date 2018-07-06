package uatransport.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uatransport.telegrambot.model.FeedbackModel;
import uatransport.telegrambot.model.Question;
import uatransport.telegrambot.repository.FeedbackModelRepository;
import uatransport.telegrambot.service.ChatModelService;
import uatransport.telegrambot.service.FeedbackModelService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackModelServiceImplementation implements FeedbackModelService {

    @Autowired
    private FeedbackModelRepository feedbackModelRepository;


    @Autowired
    private ChatModelService chatModelService;

    @Override
    public int getLastQuestionId(Long id) {

       FeedbackModel feedbackModel= feedbackModelRepository.getDistinctTopByChatModelOrderByDateDesc(chatModelService.findById(new Long(id)));
      if(feedbackModel.getQuestion()== null){
          return 0;
      }else return feedbackModel.getQuestion().getId();
    }

    @Override
    public void save(FeedbackModel feedbackModel) {
        feedbackModelRepository.save(feedbackModel);

    }

    //public int getLastQuestionId(Long chatId){
        // chatModelService.findById(chatId);
        //List<FeedbackModel> feedbackModels = feedbackModelRepository.getFeedbackModelByChatModel(chatModelService.findById(chatId));L
        //
        //
        //List<Question> questions= new ArrayList<>(feedbackModels.size());
        //int i=0;
        //for (Question question : questions) {
        //question=feedbackModels.get(i).getQuestion();
        //i++;
        //
        //}
        //
        //
        //
        //
        //List<Integer> questionIdList = new ArrayList<>(feedbackModels.size());
        //
        //for (Integer integer : questionIdList) {
        //integer=questions.get(i).getId(); }
     //   return 3;
   // }
}
