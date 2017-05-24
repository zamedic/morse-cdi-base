package com.marcarndt.morse.command.commandlets;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;

/**
 * Created by arndt on 2017/04/18.
 */
public interface Commandlet {

  public boolean canHandleCommand(Message message, String state);

  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot);

  public String getNewState(Message message, String command);

  List<String> getNewStateParams(Message message, String state, List<String> parameters);
}
