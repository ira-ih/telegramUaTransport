package uatransport.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uatransport.telegrambot.model.ChatModel;
import uatransport.telegrambot.repository.ChatModelRepository;
import uatransport.telegrambot.service.ChatModelService;

import javax.transaction.Transactional;

@Service
public class ChatModelServiceImpl implements ChatModelService {

    @Autowired
    private ChatModelRepository chatModelRepository;

    @Override
    public void save(ChatModel chatModel) {
        chatModelRepository.saveAndFlush(chatModel);
    }

    @Override
    @Transactional
    public void updateTransportType(long id,int type) {
       ChatModel chatModel= chatModelRepository.findByChatId(id);
       chatModel.setTransportType(type);
        chatModelRepository.saveAndFlush(chatModel);


    }

    @Override
    @Transactional
    public void updateTransportNumber(long id,int number) {
        ChatModel chatModel= chatModelRepository.findByChatId(id);
        chatModel.setTransportNumber(number);
        chatModelRepository.saveAndFlush(chatModel);



    }

    @Override
    public ChatModel findById(long id) {
        return chatModelRepository.findByChatId(id);
    }
}
