package org.redstart.gamemechanics;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.redstart.SocketHandler;
import org.redstart.message.MessageHandler;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameRoomExecutor {

    private static final Logger log = Logger.getLogger(GameRoomExecutor.class.getName());
    private final MessageHandler messageHandler;
    private final Map<SocketChannel, GameRoom> gameRooms;

    private final ExecutorService executorService;

    private SocketHandler socketHandler;

    private GameLogic gameLogic;

    public GameRoomExecutor(MessageHandler messageHandler, int nThreads) {
        this.messageHandler = messageHandler;
        gameRooms = new ConcurrentHashMap<>();
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void createGameRoom(SocketChannel socketChannel) {
        executorService.execute(() -> {

            GameRoom gameRoom = new GameRoom(gameLogic);
            gameRoom.getMonster().setNewTimeCreation();
            try {
                byte[] message = messageHandler.objectToJson(gameRoom.getAdventureData());
                socketHandler.writeToBuffer(socketChannel, message);
            } catch (JsonProcessingException e) {
                log.log(Level.WARNING, "JSON error");
            }
            gameRoom.getPlayer().getSpawnedBlocks().clear();
            gameRooms.put(socketChannel, gameRoom);
        });
    }

    public void removeGameRoom(SocketChannel socketChannel) {
        executorService.execute(() -> {
            gameRooms.remove(socketChannel);
            log.info("Count game room object in map - " + gameRooms.size());
        });
    }

    public GameRoom getGameRoom(SocketChannel socketChannel) {
        return gameRooms.get(socketChannel);
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public Map<SocketChannel, GameRoom> getGameRooms() {
        return gameRooms;
    }
}
