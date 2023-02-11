package org.redstart.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
//        int firstN = message.indexOf('\n');
//        String str;
//        if (firstN == -1) {
//            str = message.replaceAll("\\D", "");
//        } else {
//            str = message.substring(0, firstN).replaceAll("\\D", "");
//        }
        String str = message.replaceAll("[^\\d-]", "");
        int number = 0;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.log(Level.INFO, "Error convert String to Int. " + e.getMessage());
        }
        return number;
    }
}
