package org.redstart;

import org.redstart.gamemechanics.GameLogicExecutor;
import org.redstart.gamemechanics.GameRoomExecutor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketHandler implements Runnable {

    private static final Logger log = Logger.getLogger(SocketHandler.class.getName());

    private final ServerSocketChannel serverSocketChannel;

    private final Map<SocketChannel, SocketClient> channels;

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
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) continue;

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isValid()) {
                        SocketChannel socketChannel = null;
                        try {
                            if (key.isAcceptable()) {
                                acceptChannel();
                            }

                            if (key.isReadable()) {
                                socketChannel = (SocketChannel) key.channel();
                                readChannel(socketChannel);
                            }

                            if (key.isWritable()) {
                                socketChannel = (SocketChannel) key.channel();
                                writeChannel(socketChannel);
                            }
                        } catch (CancelledKeyException e) {
                            log.log(Level.INFO, "Client is disconnect");
                            channels.remove(socketChannel);
                            gameRoomExecutor.removeGameRoom(socketChannel);
                            log.info("Count connection object in map - " + channels.size());
                        } catch (IOException e) {
                            log.log(Level.INFO, "Connection close " + socketChannel);
                            try {
                                socketChannel.close();
                                channels.remove(socketChannel);
                                gameRoomExecutor.removeGameRoom(socketChannel);
                                log.info("Count connection object in map - " + channels.size());
                            } catch (IOException ex) {
                                log.log(Level.WARNING, "Connection close error ");
                            }
                        }
                    }
                }
                selector.selectedKeys().clear();
            }


        } catch (IOException e) {
            log.log(Level.SEVERE, "Error open selector");
        }
    }

    private void acceptChannel() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            log.info("Connected " + socketChannel);

            SocketClient socketClient = new SocketClient(socketChannel);

            channels.put(socketChannel, socketClient);

            gameRoomExecutor.createGameRoom(socketChannel);
        } catch (IOException e) {
            log.log(Level.INFO, "Can not connect client");
        }
    }

    private void readChannel(SocketChannel socketChannel) throws IOException {
        SocketClient socketClient = channels.get(socketChannel);
        ByteBuffer readBuffer = socketClient.getReadBuffer();

        int bytesRead = socketChannel.read(readBuffer);

        if (bytesRead == -1) {
            log.info("Connection close " + socketChannel);
            throw new IOException();
        } else if (bytesRead > 0 && readBuffer.get(readBuffer.position() - 1) == '\n') {
            readBuffer.flip();
            //byte[] bytes = new byte[readBuffer.remaining()];
            //readBuffer.get(bytes);      //получаем массив байтов

            //TODO избавиться от постоянного создания стринг
            String clientMessage = new String(readBuffer.array(), readBuffer.position(), readBuffer.limit());
//            log.info("message from client - " + clientMessage.replaceAll("[\\n\\r]", ""));
            gameLogicExecutor.addTasksToExecute(socketChannel, clientMessage);

            readBuffer.clear();
        }
    }

    private void writeChannel(SocketChannel socketChannel) throws IOException {
        SocketClient socketClient;
        if ((socketClient = channels.get(socketChannel)) != null) {
            ByteBuffer writeBuffer = socketClient.getWriteBuffer();

            if (writeBuffer.position() == 0) {
                //byte[] bytesToWrite = socketClient.getWriteToSocketQueue().poll();
                ByteBufferWrap bufferWrap = socketClient.bufferWraps.poll();
                byte[] bytesToWrite = bufferWrap.bytes;
                if (bytesToWrite != null) {
                    writeBuffer.put(ByteBuffer.wrap(bytesToWrite));
                    if (bytesToWrite.length != 0) {
                        if ((bytesToWrite[bytesToWrite.length - 1]) != '\n') {
                            writeBuffer.put((byte) '\n');
                        }
                    }
                    long l = System.currentTimeMillis() - bufferWrap.timeCreation;
                    if (l > 10) {
                        System.out.println(l);
                    }

                }
                writeBuffer.flip();
            }

            socketChannel.write(writeBuffer);

            if (!writeBuffer.hasRemaining()) {
                writeBuffer.compact();
                socketChannel.register(selector, SelectionKey.OP_READ);
                writeBuffer.clear();
                if (!socketClient.bufferWraps.isEmpty()) {
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                }
            }
        }
    }

    public void writeToBuffer(SocketChannel socketChannel, byte[] bytesToWrite) {
        SocketClient socketClient;
        if ((socketClient = channels.get(socketChannel)) != null) {
            socketClient.addToWriteQueue(bytesToWrite);
            //TODO проверить, если 10 раз положить в очередь и один раз вызвать read, то сколько считает
            try {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            } catch (ClosedChannelException e) {
                log.log(Level.INFO, "Connection is close");
            }
        }
    }

    public Map<SocketChannel, SocketClient> getChannels() {
        return channels;
    }
}
