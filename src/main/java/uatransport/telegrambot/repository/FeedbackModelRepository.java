package uatransport.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uatransport.telegrambot.entity.FeedbackModel;

import java.util.List;

public interface FeedbackModelRepository extends JpaRepository<FeedbackModel,Integer> {

    List<FeedbackModel> findAllByChatId(Long chatId);

    void deleteAllByChatId(Long chatId);

   // FeedbackModel getDistinctTopByChatModelOrderByDateDesc(ChatModel chatModel);

    FeedbackModel getDistinctTopByChatIdOrderByDateDesc(long chatId);


}
