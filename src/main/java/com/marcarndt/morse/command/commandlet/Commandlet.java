package com.marcarndt.morse.command.commandlet;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;

/**
 * Created by arndt on 2017/04/18.
 */
public interface Commandlet {

  /**
   * Can handle command boolean.
   *
   * @param message the message
   * @param state the state
   * @return the boolean
   */
  public boolean canHandleCommand(Message message, String state);

  /**
   * Handle command.
   *
   * @param message the message
   * @param state the state
   * @param parameters the parameters
   * @param morseBot the morse bot
   */
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot);

  /**
   * Gets new state.
   *
   * @param message the message
   * @param command the command
   * @return the new state
   */
  public String getNewState(Message message, String command);

  /**
   * Gets new state params.
   *
   * @param message the message
   * @param state the state
   * @param parameters the parameters
   * @return the new state params
   */
  List<String> getNewStateParams(Message message, String state, List<String> parameters);
}
