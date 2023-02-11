package org.redstart;

import org.redstart.gamemechanics.GameLogic;
import org.redstart.gamemechanics.GameLogicExecutor;
import org.redstart.gamemechanics.GameLoop;
import org.redstart.gamemechanics.GameRoomExecutor;
import org.redstart.message.MessageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {

    private static final Logger log = Logger.getLogger(Server.class.getName());

    private static final int PORT = 8080;

    public static void main(String[] args) {

        try {
            LogManager.getLogManager().readConfiguration(
                    Server.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(1);

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            MessageHandler messageHandler = new MessageHandler();

            GameLogic gameLogic = new GameLogic();

            GameRoomExecutor gameRoomExecutor = new GameRoomExecutor(messageHandler, 1);
            gameRoomExecutor.setGameLogic(gameLogic);

            GameLogicExecutor gameLogicExecutor = new GameLogicExecutor(messageHandler, gameRoomExecutor, 1);
            gameLogicExecutor.setGameLogic(gameLogic);

            SocketHandler socketHandler = new SocketHandler(serverSocketChannel, gameRoomExecutor, gameLogicExecutor);

            GameLoop gameLoop = new GameLoop(/*messageHandler,*/ gameRoomExecutor.getGameRooms());
            gameLoop.setGameLogicExecutor(gameLogicExecutor);
//            gameLoop.setChannels(socketHandler.getChannels());
//            gameLoop.setSocketHandler(socketHandler);
            new Thread(gameLoop).start();

            executorService.execute(socketHandler);

            while (true);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Server socket channel IOException", e);
        }
    }
}
