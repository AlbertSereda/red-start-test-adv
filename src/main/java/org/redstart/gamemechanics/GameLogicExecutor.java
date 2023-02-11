package org.redstart.gamemechanics;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.redstart.SocketHandler;
import org.redstart.message.MessageHandler;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
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

    public void addTasksToExecute(SocketChannel socketChannel, String message) {

        executorService.execute(() -> {
            String[] messageSplit = message.split("\n");
            String lastString = "";

            for (int i = 0; i < messageSplit.length; i++) {

                if (!lastString.equals(messageSplit[i])) {
                    executeMove(socketChannel, Move.PLAYER, messageSplit[i]);
                    lastString = messageSplit[i];
                }
            }
        });
    }

    public void executeMove(SocketChannel socketChannel, Move whoMove, String message) {
        executorService.execute(() -> {
            if (!socketChannel.isConnected()) {
                return;
            }
            GameRoom gameRoom = gameRoomExecutor.getGameRoom(socketChannel);
            Lock lock = gameRoom.getLock();
            lock.lock();
            try {
                if (gameRoom != null) {
                    switch (whoMove) {
                        case PLAYER:
                            int clientMessage = messageHandler.jsonToMessage(message);
                            if (clientMessage < 0) {
                                log.info("message - " + clientMessage);
                                gameLogic.spellMove(gameRoom, clientMessage);
                            }
                            if (clientMessage > 0) gameLogic.playerMove(gameRoom, clientMessage);
                            break;
                        case MONSTER:
                            gameRoom.getMonster().getMonsterMoveLogic().monsterMove();
                            break;
                    }
                    try {
                        byte[] sendMessage = messageHandler.objectToJson(gameRoom.getAdventureData());
                        gameRoom.getPlayer().getBlastedBlocks().clear();
                        gameRoom.getPlayer().getSpawnedBlocks().clear();
                        socketHandler.writeToBuffer(socketChannel, sendMessage);
                    } catch (JsonProcessingException e) {
                        log.log(Level.WARNING, "JSON error");
                    }
                } else {
                    log.log(Level.WARNING, "Game Room = null for socketChannel - " + socketChannel);
                }
            } finally {
                lock.unlock();
            }

        });


    }

    public void executeMove(SocketChannel socketChannel, Move whoMove) {
        executeMove(socketChannel, whoMove, "");
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
}
