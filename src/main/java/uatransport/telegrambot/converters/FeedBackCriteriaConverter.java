package uatransport.telegrambot.converters;

import org.springframework.stereotype.Component;
import uatransport.telegrambot.entity.Question;
import uatransport.telegrambot.response.FeedbackCriteriaResponse;
import uatransport.telegrambot.response.QuestionResponse;

import java.util.ArrayList;

@Component
public class FeedBackCriteriaConverter {

    public ArrayList<Question> convertToQuestionArray(FeedbackCriteriaResponse[] feedbackCriteriaResponses){
        ArrayList<Question> questions = new ArrayList<>();
        for (int i = 0; i<feedbackCriteriaResponses.length; i++){
            QuestionResponse[] questionResponses = feedbackCriteriaResponses[i].getQuestions();
            for (int j = 0; j<questionResponses.length; j++) {
                Question question = new Question();
                question.setName(questionResponses[j].getName());
                question.setWeight(questionResponses[j].getWeight());
                question.setType(feedbackCriteriaResponses[i].getType());
                question.setCriterieId(feedbackCriteriaResponses[i].getId());
                questions.add(question);
            }
        }
        return questions;
    }

    public ArrayList<Question> generalArrayList(ArrayList<Question> list1,ArrayList<Question> list2){
       ArrayList<Question> questions = new ArrayList<>();
       questions.addAll(list1);
       questions.addAll(list2);
       return questions;
    }

    public ArrayList<Question> setNextQuestionAndChatIdAndUuid(ArrayList<Question> questions, Long chatId, String uuid){
        for (int i=0; i<questions.size()-1; i++){
            questions.get(i).setChatId(chatId).setNextQuestion(questions.get(i+1).setUuid(uuid));
        }
        return questions;
    }

}
