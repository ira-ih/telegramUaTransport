package uatransport.telegrambot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import uatransport.telegrambot.converters.CategoryConverter;
import uatransport.telegrambot.converters.FeedBackConverter;
import uatransport.telegrambot.converters.FeedBackCriteriaConverter;
import uatransport.telegrambot.entity.ChatModel;
import uatransport.telegrambot.entity.FeedbackModel;
import uatransport.telegrambot.entity.Question;
import uatransport.telegrambot.models.Category;
import uatransport.telegrambot.response.mapper.CategoryResponseMapper;
import uatransport.telegrambot.response.mapper.FeedbackCriteriaResponseMapper;
import uatransport.telegrambot.response.mapper.TransitResponseMapper;
import uatransport.telegrambot.service.ChatModelService;
import uatransport.telegrambot.service.FeedbackModelService;
import uatransport.telegrambot.service.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UaTransportBot extends TelegramLongPollingBot {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedbackModelService feedbackModelService;

    @Autowired
    private ChatModelService chatModelService;


    @Autowired
    private CategoryResponseMapper categoryResponseMapper;

    @Autowired
    FeedbackCriteriaResponseMapper feedbackCriteriaResponseMapper;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private FeedBackCriteriaConverter feedBackCriteriaConverter;

    @Autowired
    private TransitResponseMapper transitResponseMapper;

    @Autowired
    private FeedBackConverter feedBackConverter;


    @Autowired
    private KeyBoard keyBoard;


    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getChat().getFirstName();
        String message = update.getMessage().getText();

        if (message.equals("/start")) {
            sendMsg("Привіт " + name +
                    "! Будь ласка , використайте  /feedback команду щоб залишити враження від поїздки", chatId);
        } else if (message.equals("/feedback")) {
            chatModelService.save(new ChatModel().setChatId(chatId));
            ArrayList<Category> categories = categoryConverter.convertToCategoryArray(categoryResponseMapper.getCategoryList());


            sendMsgWithDynamicKeyboard("Будь ласка, виберіть тип транспорту", chatId, categories);
        } else if (chatModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getCategoryName() == null) {

            Integer categoryId = categoryConverter.convertSingleCategory(categoryResponseMapper.getSingleCategory(message)).getId();


            chatModelService.updateCategoryName(message, categoryId, chatId);
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            feedbackModelService.save(new FeedbackModel().setChatId(chatId).setUuid(uuid));
            sendMsg("Будь ласка, вкажіть номер транспорту", update.getMessage().getChatId());
        } else if (feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getTransitId() == null) {

            Integer categoryId = chatModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getCategoryId();
            Integer transitId = transitResponseMapper.getTransitByNameAndNextLevelCategoryName(message, categoryId).getId();
            feedbackModelService.updateTransitId(transitId, chatId);

            ArrayList<Question> questions1 = feedBackCriteriaConverter.convertToQuestionArray(feedbackCriteriaResponseMapper.getFeedbackCriteriaResponseByCategoryIdAndTypeSIMPLE(chatModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getCategoryId()));
            ArrayList<Question> questions2 = feedBackCriteriaConverter.convertToQuestionArray(feedbackCriteriaResponseMapper.getFeedbackCriteriaResponseByCategoryIdAndTypeRating(5));
            ArrayList<Question> questions = feedBackCriteriaConverter.setNextQuestionAndChatIdAndUuid(feedBackCriteriaConverter.generalArrayList(questions1, questions2), chatId, feedbackModelService.currentUuid(chatId));


            questionService.saveQuestionFromList(questions);
            feedbackModelService.updateQuestion(questions.get(0), chatId);
            //feedbackModelService.save(new FeedbackModel().setTransitId(feedbackModelService.currentTransitId(chatId)).setChatId(chatId).setQuestion(questionService.findByChatId(chatId).get(1)));
            sendMsgWithKeyboardByType(questions.get(0).getName(), chatId, questions.get(0).getType());

        } else if (feedbackModelService.getDistinctTopByChatIdOrderByDateDesc(chatId).getAnswer() == null) {
            feedbackModelService.updateAnswer(message, chatId);
            if (feedbackModelService.getLastQuestion(chatId).getNextQuestion() != null) {
                Question nextQuestion = feedbackModelService.getLastQuestion(chatId).getNextQuestion();
                feedbackModelService.save(new FeedbackModel().setTransitId(feedbackModelService.currentTransitId(chatId)).setChatId(chatId).setQuestion(nextQuestion).setUuid(feedbackModelService.currentUuid(chatId)));
                sendMsgWithKeyboardByType(nextQuestion.getName(), chatId, nextQuestion.getType());
            } else {
                feedbackModelService.findAllByChatId(chatId);
                feedBackConverter.executePost(feedbackModelService.findAllByChatId(chatId));

                sendMsg("Дякую, Ваш відгук прийнято", chatId);
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

    public void sendMsgWithDynamicKeyboard(String messageToSend, Long chatId, ArrayList<Category> categories) {
        SendMessage messageSend = new SendMessage()
                .setChatId(chatId)
                .setText(messageToSend);
        try {
            messageSend.setReplyMarkup(keyBoard.createKeyboardDynamic(categories));
            execute(messageSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgWithKeyboardByType(String messageToSend, Long chatId, String type) {
        SendMessage messageSend = new SendMessage()
                .setChatId(chatId)
                .setText(messageToSend);
        try {
            messageSend.setReplyMarkup(keyBoard.createKeyboardeByType(type));
            execute(messageSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Component
class KeyBoard {

    public ReplyKeyboardMarkup createKeyboardDynamic(ArrayList<Category> categories) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Category category : categories) {
            KeyboardRow row = new KeyboardRow();
            row.add(category.getName());
            keyboard.add(row);

        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createKeyboardeByType(String type) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        switch (type) {
            case "SIMPLE":
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