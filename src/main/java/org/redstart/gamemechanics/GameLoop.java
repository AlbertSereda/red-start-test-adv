package org.redstart.gamemechanics;

import org.redstart.jsonclasses.Monster;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.logging.Logger;

public class GameLoop implements Runnable {

    private static final Logger log = Logger.getLogger(GameLoop.class.getName());
    //private final MessageHandler messageHandler;

    private Map<SocketChannel, GameRoom> gameRooms;
    private GameLogicExecutor gameLogicExecutor;
    //private Map<SocketChannel, ByteBufferWrap> channels;
    //private SocketHandler socketHandler;

    public GameLoop(/*MessageHandler messageHandler,*/ Map<SocketChannel, GameRoom> gameRooms) {
        //this.messageHandler = messageHandler;
        this.gameRooms = gameRooms;
    }

    @Override
    public void run() {
        while (true) {

            for (Map.Entry<SocketChannel, GameRoom> entry : gameRooms.entrySet()) {
                SocketChannel socketChannel = entry.getKey();
                GameRoom gameRoom = entry.getValue();
                Monster monster = gameRoom.getMonster();
                monster.updateCurrentSpeed();
                if (monster.getCurrentSpeed() <= 0) {
                    gameLogicExecutor.executeMove(socketChannel, Move.MONSTER);
                    monster.setNewTimeCreation();
                }

//                ByteBufferWrap bufferWrap = channels.get(socketChannel);
//                if (bufferWrap !=null &&  bufferWrap.getIsReadyWrite().get()){
//                    Lock lock = gameRoom.getLock();
//                    if (lock.tryLock()) {
//                        try {
//                            try {
//                                byte[] json = messageHandler.objectToJson(gameRoom.getAdventureData());
//                                channels.get(socketChannel).getIsReadyWrite().set(false);
//                                socketHandler.writeToBuffer(socketChannel, json);
//                                gameRoom.getPlayer().getBlastedBlocks().clear();
//                                gameRoom.getPlayer().getSpawnedBlocks().clear();
//                            } catch (JsonProcessingException e) {
//                                log.log(Level.WARNING, "JSON error", e);
//                            }
//                        } finally {
//                            lock.unlock();
//                        }
//                    }
//                }
            }
        }
    }

    public void setGameLogicExecutor(GameLogicExecutor gameLogicExecutor) {
        this.gameLogicExecutor = gameLogicExecutor;
    }

//    public void setChannels(Map<SocketChannel, ByteBufferWrap> channels) {
//        this.channels = channels;
//    }

//    public void setSocketHandler(SocketHandler socketHandler) {
//        this.socketHandler = socketHandler;
//    }
}


