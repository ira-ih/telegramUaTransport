package uatransport.telegrambot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import uatransport.telegrambot.model.ChatModel;
import uatransport.telegrambot.model.FeedbackModel;
import uatransport.telegrambot.model.Question;
import uatransport.telegrambot.repository.FeedbackModelRepository;
import uatransport.telegrambot.repository.QuestionRepository;
import uatransport.telegrambot.service.FeedbackModelService;
import uatransport.telegrambot.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UaTransportBot extends TelegramLongPollingBot {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedbackModelService feedbackModelService;

    @Autowired
    private FeedbackModelRepository feedbackModelRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void onUpdateReceived(Update update) {

        ResponseEntity<List> questions = restTemplate.getForEntity("http://localhost:8080/feedback-criteria", List.class);

        Long chatId = update.getMessage().getChatId();
        //System.out.println(update);
        ChatModel chatModel = new ChatModel();
        chatModel.setChatId(update.getMessage().getChatId());

        String name = update.getMessage().getChat().getFirstName();

        String message = update.getMessage().getText();
        System.out.println(message + " "+ name);
        if (message.equals("/start")) {
            sendMsg("Привіт " + name +
                    "! Будь ласка , використайте  /feedback команду щоб залишити враження від поїздки", chatId);
        } else if (message.equals("/feedback")) {
            feedbackModelService.saveFedbackModel(new FeedbackModel().setChatId(chatId));
            sendMsgWithKeyboard("Будь ласка, виберіть тип транспорту", chatId, "transportType");
        } else if (feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getTransportType() == null) {
            switch (message) {
                case "Трамвай":
                    feedbackModelService.updateTransportType("Tram", chatId);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;
                case "Тролейбус":
                    feedbackModelService.updateTransportType("Troll", chatId);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;
                case "Автобус":
                    feedbackModelService.updateTransportType("Bus", chatId);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;

            }
        } else {
            if (feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getTransportNumber() == null) {

                feedbackModelService.updateTransportNumber(message, chatId);

                feedbackModelService.save(new FeedbackModel().setChatId(chatId)
                        .setTransportNumber(feedbackModelService.currentTransportNumber(chatId))
                        .setTransportType(feedbackModelService.currentTransportType(chatId)));
                int questionId = feedbackModelService.getLastQuestionId(chatId);


                FeedbackModel feedbackModel = feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId);
                feedbackModel.setQuestion(questionRepository.findById(questionId + 1));
                feedbackModelService.saveFedbackModel(feedbackModel);

                sendMsgWithKeyboard(questionExecutor(questionId), chatId, questionRepository.findById(questionId + 1).getType());


            } else {
                FeedbackModel feedbackModel = feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId);
                feedbackModel.setAnswer(message);
                feedbackModelService.saveFedbackModel(feedbackModel);
                int questionId = feedbackModelService.getLastQuestionId(chatId);

                feedbackModelService.saveFedbackModel(new FeedbackModel().setChatId(chatId)
                        .setTransportNumber(feedbackModelService.currentTransportNumber(chatId))
                        .setTransportType(feedbackModelService.currentTransportType(chatId)).setQuestion(questionRepository.findById(questionId + 1)));
                if (questionRepository.findById(questionId + 1) == null) {
                    sendMsg(questionExecutor(questionId), chatId);
                } else
                    sendMsgWithKeyboard(questionExecutor(questionId), chatId, questionRepository.findById(questionId + 1).getType());

            }

        }
    }


    @Override
    public String getBotUsername() {

        return "UaTransportBot";
    }

    @Override
    public String getBotToken() {

        return "604444787:AAEZAu56oW_KVcd2PmXmiBQsu_gMnBKgy9s";
    }


    public void sendMsg(String messageToSend, Long chatId) {
        SendMessage messageSend = new SendMessage()
                .setChatId(chatId)
                .setText(messageToSend);

        try {

            execute(messageSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgWithKeyboard(String messageToSend, Long chatId, String type) {
        SendMessage messageSend = new SendMessage()
                .setChatId(chatId)
                .setText(messageToSend);

        try {
            messageSend.setReplyMarkup(createKeyboarde(type));
            execute(messageSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String questionExecutor(int questionId) {
        List<Question> questionList = questionService.getAll();
        if (questionId < questionList.size()) {
            return questionList.get(questionId).getName();

        } else
            return "Дякую, Ваш відгук прийнято";
    }

    public ReplyKeyboardMarkup createKeyboarde(String type) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        switch (type) {
            case "transportType":
                row.add("Автобус");
                keyboard.add(row);

                row = new KeyboardRow();
                row.add("Трамвай");
                keyboard.add(row);

                row = new KeyboardRow();
                row.add("Тролейбус");
                keyboard.add(row);
                keyboardMarkup.setKeyboard(keyboard);
                break;
            case "ACCEPTANCE":
                row.add("Так");
                keyboard.add(row);

                row = new KeyboardRow();
                row.add("Ні");
                keyboard.add(row);
                keyboardMarkup.setKeyboard(keyboard);
                break;
            case "RATING":
                row.add("1");
                row.add("2");
                row.add("3");
                row.add("4");
                row.add("5");
                keyboard.add(row);
                row = new KeyboardRow();
                row.add("6");
                row.add("7");
                row.add("8");
                row.add("9");
                row.add("10");
                keyboard.add(row);
                keyboardMarkup.setKeyboard(keyboard);

        }
        return keyboardMarkup;

    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
