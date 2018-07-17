package uatransport.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uatransport.telegrambot.entity.ChatModel;


public interface ChatModelRepository extends JpaRepository<ChatModel, Integer> {
 ChatModel findByChatId(Long chatId);
 void deleteByChatId(Long chatId);

 ChatModel getDistinctTopByChatIdOrderByDateDesc(long chatId);



}
