package uatransport.telegrambot.service;

import uatransport.telegrambot.model.ChatModel;

public interface ChatModelService {
    public void save(ChatModel chatModel);
    public void updateTransportType(long id, int type);
    public void updateTransportNumber(long id,int number);
    public ChatModel findById(long id);
}
