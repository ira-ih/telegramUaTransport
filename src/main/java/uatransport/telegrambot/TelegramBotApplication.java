package uatransport.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import uatransport.telegrambot.bot.UaTransportBot;


@SpringBootApplication
public class TelegramBotApplication {

    public static void main(String[] args) {

        ApiContextInitializer.init();
       ConfigurableApplicationContext context= SpringApplication.run(TelegramBotApplication.class, args);

       System.out.println("Application started");

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(context.getBean(UaTransportBot.class));
        } catch (
                TelegramApiException e)

        {
            e.printStackTrace();
        }



    }
}
