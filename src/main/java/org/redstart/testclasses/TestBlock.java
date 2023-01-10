package org.redstart.testclasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redstart.gamemechanics.GameLogic;
import org.redstart.gamemechanics.GameRoom;

public class TestBlock {
    public static void main(String[] args) {
        GameRoom gameRoom = new GameRoom(new GameLogic());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            System.out.println(objectMapper.writeValueAsString(gameRoom));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
