package uatransport.telegrambot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import uatransport.telegrambot.service.ChatModelService;
import uatransport.telegrambot.service.FeedbackModelService;
import uatransport.telegrambot.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UaTransportBot extends TelegramLongPollingBot {

    @Autowired
    private ChatModelService chatModelService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedbackModelService feedbackModelService;

    @Autowired
    private FeedbackModelRepository feedbackModelRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();
        System.out.println(update);
        ChatModel chatModel = new ChatModel();
        chatModel.setChatId(update.getMessage().getChatId());

        String name = update.getMessage().getChat().getFirstName();

        String message = update.getMessage().getText();
        System.out.println(message);
        if (message.equals("/start")) {
            chatModelService.save(chatModel);
            sendMsg("Привіт " + name +
                    "! Будь ласка , використайте  /feedback команду щоб залишити враження від поїздки", chatId);
        }else if (message.equals("/feedback")) {

            // Add it to the message

            sendMsgWithKeyboard("Будь ласка, виберіть тип транспорту", chatId, "transportType");
        }else if (chatModelService.findById(chatId).getTransportType()==null) {
            switch (message) {
                case "Трамвай":
                    chatModelService.updateTransportType(chatId, 1);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;
                case "Тролейбус":
                    chatModelService.updateTransportType(chatId, 2);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;
                case "Автобус":
                    chatModelService.updateTransportType(chatId, 3);
                    sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
                    break;

            }
        } else {
            if (chatModelService.findById(chatId).getTransportNumber() == null) {
                int transportNumber = Integer.parseInt(message);
                chatModelService.updateTransportNumber(chatId, transportNumber);

                feedbackModelService.save(new FeedbackModel().setChatModel(chatModelService.findById(chatId)));
                int k = feedbackModelService.getLastQuestionId(chatId);


                FeedbackModel feedbackModel = feedbackModelRepository.getDistinctTopByChatModelOrderByDateDesc(chatModelService.findById(chatId));
                feedbackModel.setQuestion(questionRepository.findById(k + 1));
                feedbackModelRepository.save(feedbackModel);

                sendMsgWithKeyboard(questionExecutor(k), chatId, questionRepository.findById(k + 1).getType());


            } else {
                FeedbackModel feedbackModel = feedbackModelRepository.getDistinctTopByChatModelOrderByDateDesc(chatModelService.findById(chatId));
                feedbackModel.setAnswer(message);
                feedbackModelRepository.save(feedbackModel);
                int k = feedbackModelService.getLastQuestionId(chatId);

                feedbackModelService.save(new FeedbackModel().setChatModel(chatModelService.findById(chatId)).setQuestion(questionRepository.findById(k + 1)));
                if (questionRepository.findById(k + 1) == null) {
                    sendMsg(questionExecutor(k), chatId);
                } else
                    sendMsgWithKeyboard(questionExecutor(k), chatId, questionRepository.findById(k + 1).getType());

            }

        }
    }
        /*if (message.startsWith("*")) {
            FeedbackModel feedbackModel = feedbackModelRepository.getDistinctTopByChatModelOrderByDateDesc(chatModelService.findById(chatId));
            feedbackModel.setAnswer(message);
            feedbackModelRepository.save(feedbackModel);
            int k = feedbackModelService.getLastQuestionId(chatId);

            feedbackModelService.save(new FeedbackModel().setChatModel(chatModelService.findById(chatId)).setQuestion(questionRepository.findById(k + 1)));
if(questionRepository.findById(k+1)==null){
    sendMsg(questionExecutor(k), chatId);
}
else
    sendMsgWithKeyboard(questionExecutor(k), chatId,questionRepository.findById(k+1).getType());

        }*/





    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @UaTransportBot, it must return 'UaTransportBot'
        return "UaTransportBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "604444787:AAEZAu56oW_KVcd2PmXmiBQsu_gMnBKgy9s";
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void sendMsg(String messageToSend, Long chatId) {
        SendMessage messageSend = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText(messageToSend);

        try {

            execute(messageSend); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgWithKeyboard(String messageToSend, Long chatId, String type) {
        SendMessage messageSend = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText(messageToSend);

        try {
            messageSend.setReplyMarkup(createKeyboarde(type));
            execute(messageSend); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String questionExecutor(int i) {
        List<Question> questionList = questionService.getAll();
        if (i == questionList.size()) {
            return "Дякую, Ваш відгук прийнято";
        } else if (i > questionList.size()) {
            return "Дякую, Ваш відгук прийнято";
        } else if (i < questionList.size()) {
            return questionList.get(i).getName();

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
}
