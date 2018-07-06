package uatransport.telegrambot.service;

import uatransport.telegrambot.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getAll();
}
