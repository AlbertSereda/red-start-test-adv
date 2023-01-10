package org.redstart.massage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redstart.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageHandler {
    private static final Logger log = Logger.getLogger(MessageHandler.class.getName());

    private final ObjectMapper objectMapper;

    public MessageHandler() {
        objectMapper = new ObjectMapper();
    }

    public byte[] objectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
    }

    public int jsonToMessage(String message) {
        //log.info("Message from client - " + message.replaceAll("[\r\n]]", ""));
        String str = message.replaceAll("\\D", "");
        int number = 0;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.log(Level.INFO, "Error convert String to Int. " + e.getMessage());
        }
        return number;
    }
}
