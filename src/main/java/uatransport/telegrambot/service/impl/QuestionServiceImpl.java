package uatransport.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uatransport.telegrambot.entity.Question;
import uatransport.telegrambot.repository.QuestionRepository;
import uatransport.telegrambot.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;




    @Override
    @Transactional
    public List<Question> findByChatId(long chatId) {
        return questionRepository.findByChatId(chatId);
    }

    @Transactional
    public void saveQuestionFromList(ArrayList<Question> questions){
        for (Question question : questions) {
            questionRepository.save(question);

        }
    }

    @Override
    @Transactional
    public void deleteListQuestionsByChatId(Long chatId) {
        questionRepository.findByChatId(chatId);
        for (int i =0; i<questionRepository.findByChatId(chatId).size(); i++){
         questionRepository.delete( questionRepository.findByChatIdOrderByIdAsc(chatId).get(i+1));
        }
    }
}
