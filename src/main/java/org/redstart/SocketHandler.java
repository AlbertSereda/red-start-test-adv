package org.redstart;

import org.redstart.gamemechanics.GameLogicExecutor;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.gamemechanics.GameRoomExecutor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketHandler implements Runnable {

    private static final Logger log = Logger.getLogger(SocketHandler.class.getName());

    private static final int READ_BUFFER_CAPACITY = 1000;
    private static final int WRITE_BUFFER_CAPACITY = 2000;

    private final ServerSocketChannel serverSocketChannel;

    private final Map<SocketChannel, ByteBufferWrap> channels;

    private final GameRoomExecutor gameRoomExecutor;

    private final GameLogicExecutor gameLogicExecutor;

    private Selector selector;

    public SocketHandler(ServerSocketChannel serverSocketChannel, GameRoomExecutor gameRoomExecutor, GameLogicExecutor gameLogicExecutor) {
        this.serverSocketChannel = serverSocketChannel;

        this.gameRoomExecutor = gameRoomExecutor;
        gameRoomExecutor.setSocketHandler(this);

        this.gameLogicExecutor = gameLogicExecutor;
        gameLogicExecutor.setSocketHandler(this);


        channels = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            log.info("Server is started");

            while (true) {
                //selector.select();

                int readyChannels = selector.selectNow();
                if (readyChannels == 0) continue;

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isValid()) {
                        try {
                            if (key.isAcceptable()) {
                                acceptChannel(key);
                            }

                            if (key.isReadable()) {
                                readChannel(key);
                            }

                            if (key.isWritable()) {
                                writeChannel(key);
                            }
                        } catch (CancelledKeyException e) {
                            log.log(Level.INFO, "Client is disconnect", e);
                        }
                    }
                }
                selector.selectedKeys().clear();
            }


        } catch (IOException e) {
            log.log(Level.SEVERE, "Error open selector", e);
        }
    }

    private void acceptChannel(SelectionKey key) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            log.info("Connected " + socketChannel);

            ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUFFER_CAPACITY);
            ByteBuffer writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_CAPACITY);

            ByteBufferWrap bufferWrap = new ByteBufferWrap(readBuffer, writeBuffer);

            channels.put(socketChannel, bufferWrap);

            gameRoomExecutor.createGameRoom(socketChannel);
        } catch (IOException e) {
            log.log(Level.INFO, "Can not connect client", e);
        }
    }

    private void readChannel(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            ByteBufferWrap bufferWrap = channels.get(socketChannel);
            ByteBuffer readBuffer = bufferWrap.getReadBuffer();

            int bytesRead = socketChannel.read(readBuffer);

            if (bytesRead == -1) {
                log.info("Connection close " + socketChannel);
                socketChannel.close();
                channels.remove(socketChannel);
                gameRoomExecutor.removeGameRoom(socketChannel);
                log.info("Count connection object in map - " + channels.size());
            } else if (bytesRead > 0 && readBuffer.get(readBuffer.position() - 1) == '\n') {
                readBuffer.flip();

                //byte[] bytes = new byte[readBuffer.remaining()];
                //readBuffer.get(bytes);      //получаем массив байтов

                String clientMessage = new String(readBuffer.array(), readBuffer.position(), readBuffer.limit());
                gameLogicExecutor.executePlayerMove(socketChannel, clientMessage);

                readBuffer.clear();
            }
        } catch (IOException e) {
            log.log(Level.INFO, "Connection close " + socketChannel, e);
            try {
                socketChannel.close();
            } catch (IOException ex) {
                log.log(Level.WARNING, "Connection close error ", ex);
            }
        }
    }

    private void writeChannel(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            ByteBuffer writeBuffer = channels.get(socketChannel).getWriteBuffer();

            socketChannel.write(writeBuffer);

            if (!writeBuffer.hasRemaining()) {
                writeBuffer.compact();
                socketChannel.register(selector, SelectionKey.OP_READ);
                writeBuffer.clear();
            }
        } catch (IOException e) {
            log.log(Level.INFO, "Connection close" + socketChannel, e);
            try {
                socketChannel.close();
            } catch (IOException ex) {
                log.log(Level.WARNING, "Connection close error", ex);
            }
        }
    }

    public void writeToBuffer(SocketChannel socketChannel, byte[] bytesToWrite) {
        if (channels.containsKey(socketChannel)) {
            ByteBuffer writeBuffer = channels.get(socketChannel).getWriteBuffer();

            writeBuffer.put(ByteBuffer.wrap(bytesToWrite));

            if (bytesToWrite.length != 0) {
                if ((bytesToWrite[bytesToWrite.length - 1]) != 10) {
                    writeBuffer.put((byte) 10);
                }
            }

            writeBuffer.flip();

            try {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            } catch (ClosedChannelException e) {
                log.log(Level.INFO, "Connection is close", e);
            }

        }
    }
}
