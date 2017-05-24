package com.marcarndt.morse.telegrambots;

import com.marcarndt.morse.telegrambots.generics.BotSession;
import com.marcarndt.morse.telegrambots.generics.Webhook;
import com.marcarndt.morse.telegrambots.updatesreceivers.DefaultBotSession;
import com.marcarndt.morse.telegrambots.updatesreceivers.DefaultWebhook;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief TODO
 * @date 29 of October of 2016
 */
public class ApiContextInitializer {
    private ApiContextInitializer() {
    }

    public static void init() {
        ApiContext.register(BotSession.class, DefaultBotSession.class);
        ApiContext.register(Webhook.class, DefaultWebhook.class);
    }
}
