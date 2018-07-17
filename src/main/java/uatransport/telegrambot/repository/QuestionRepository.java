package uatransport.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uatransport.telegrambot.entity.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findById(int id);

    void delete(Question question);

    List<Question> findByChatId(long id);
    List<Question> findByChatIdOrderByIdAsc(long id);

}



