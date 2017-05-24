package com.marcarndt.morse.telegrambots.api.methods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.marcarndt.morse.telegrambots.api.interfaces.Validable;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;

import java.io.Serializable;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Api method that can't be use completely as Json
 */
public abstract class PartialBotApiMethod<T extends Serializable> implements Validable {
    @JsonIgnore
    protected static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Deserialize a json answer to the response type to a method
     * @param answer Json answer received
     * @return Answer for the method
     */
    public abstract T deserializeResponse(String answer) throws TelegramApiRequestException;
}
