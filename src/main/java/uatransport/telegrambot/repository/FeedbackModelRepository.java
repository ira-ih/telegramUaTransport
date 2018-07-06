package uatransport.telegrambot.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uatransport.telegrambot.model.ChatModel;
import uatransport.telegrambot.model.FeedbackModel;
import uatransport.telegrambot.model.Question;

import java.util.List;

public interface FeedbackModelRepository extends JpaRepository<FeedbackModel,Integer> {

    List<FeedbackModel> findAllByChatModel(Long chatId);

    FeedbackModel getDistinctTopByChatModelOrderByDateDesc(ChatModel chatModel);

}
