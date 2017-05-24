package com.marcarndt.morse.telegrambots.generics;

import com.marcarndt.morse.telegrambots.api.objects.Update;
import com.marcarndt.morse.telegrambots.bots.DefaultBotOptions;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Callback to handle updates.
 * @date 20 of June of 2015
 */
public interface LongPollingBot {
    /**
     * This method is called when receiving updates via GetUpdates method
     * @param update Update received
     */
    void onUpdateReceived(Update update);

    /**
     * Return bot username of this bot
     */
    String getBotUsername();

    /**
     * Return bot token to access Telegram API
     */
    String getBotToken();

    /**
     * Gets options for current bot
     * @return BotOptions object with options information
     */
    DefaultBotOptions getOptions();

    /**
     * Clear current webhook (if present) calling setWebhook method with empty url.
     */
    void clearWebhook() throws TelegramApiRequestException;

    /**
     * Called when the BotSession is being closed
     */
    default void onClosing() {
    }
}
