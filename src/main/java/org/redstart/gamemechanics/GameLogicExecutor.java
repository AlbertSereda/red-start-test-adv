package org.redstart.gamemechanics;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.redstart.Server;
import org.redstart.SocketHandler;
import org.redstart.massage.MessageHandler;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogicExecutor {

    private static final Logger log = Logger.getLogger(GameLogicExecutor.class.getName());
    private final MessageHandler messageHandler;
    private GameLogic gameLogic;
    private final ExecutorService executorService;
    private SocketHandler socketHandler;

    private final GameRoomExecutor gameRoomExecutor;

    public GameLogicExecutor(MessageHandler messageHandler, GameRoomExecutor gameRoomExecutor, int nThreads) {
        this.messageHandler = messageHandler;
        this.gameRoomExecutor = gameRoomExecutor;
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void executePlayerMove(SocketChannel socketChannel, String message) {

        executorService.execute(() -> {

            int nameBlockDestroyed = messageHandler.jsonToMessage(message);
            GameRoom gameRoom = gameRoomExecutor.getGameRoom(socketChannel);
            if (gameRoom != null) {
                gameLogic.playerMove(gameRoom, nameBlockDestroyed);
                try {
                    byte[] json = messageHandler.objectToJson(gameRoom.getAdventureData());
                    socketHandler.writeToBuffer(socketChannel, json);
                } catch (JsonProcessingException e) {
                    log.log(Level.WARNING, "JSON error", e);
                }
            } else {
                log.log(Level.WARNING, "Game Room = null for socketChannel - " + socketChannel + ". The gameRoom was not to sent the socketChannel");
            }

        });
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
}
