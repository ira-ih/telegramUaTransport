package uatransport.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uatransport.telegrambot.model.ChatModel;


public interface ChatModelRepository extends JpaRepository<ChatModel, Integer> {
 ChatModel findByChatId(Long chatId);
 void deleteByChatId(Long chatId);



}
