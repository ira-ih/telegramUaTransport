package uatransport.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uatransport.telegrambot.model.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findById(int id);
}



