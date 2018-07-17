package uatransport.telegrambot.service;

import uatransport.telegrambot.entity.Question;

import java.util.ArrayList;
import java.util.List;

public interface QuestionService {

    List<Question> findByChatId(long chatId);

    public void saveQuestionFromList(ArrayList<Question> questions);

    void deleteListQuestionsByChatId(Long chatId);
}
