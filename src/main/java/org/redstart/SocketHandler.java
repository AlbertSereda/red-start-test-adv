package org.redstart;

import org.redstart.gamemechanics.GameLogicExecutor;
import org.redstart.gamemechanics.GameRoomExecutor;
import org.redstart.gamemechanics.Move;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) continue;

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isValid()) {
                        SocketChannel socketChannel = null;
                        try {
                            if (key.isAcceptable()) {
                                acceptChannel(key);
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
            log.log(Level.INFO, "Can not connect client");
        }
    }

    private void readChannel(SocketChannel socketChannel) throws IOException {
        ByteBufferWrap bufferWrap = channels.get(socketChannel);
        ByteBuffer readBuffer = bufferWrap.getReadBuffer();

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
            //System.out.println("client message - " + clientMessage);
            gameLogicExecutor.addTasksToExecute(socketChannel, clientMessage);

            readBuffer.clear();
        }
    }

    private void writeChannel(SocketChannel socketChannel) throws IOException {
        ByteBufferWrap bufferWrap = channels.get(socketChannel);
        ByteBuffer writeBuffer = bufferWrap.getWriteBuffer();

        socketChannel.write(writeBuffer);

        if (!writeBuffer.hasRemaining()) {
            writeBuffer.compact();
            socketChannel.register(selector, SelectionKey.OP_READ);
            writeBuffer.clear();
            bufferWrap.getIsReadyWrite().set(true);
        }
    }
    //TODO изменить способ отправки
    //новые данные для отправки будем помещать в очередь, которая будет храниться у SocketClient
    //мапу channels переделать, что бы она хранила <SocketChannel, SocketClient>
    //после того как данные поместились в очередь, помечаем что можно писать в сокет
    //writeChannel будет брать из очереди данные и отправлять их

    public void writeToBuffer(SocketChannel socketChannel, byte[] bytesToWrite) {
        if (channels.containsKey(socketChannel)) {
            ByteBufferWrap bufferWrap = channels.get(socketChannel);
            ByteBuffer writeBuffer = bufferWrap.getWriteBuffer();
            while (bufferWrap !=null &&  !bufferWrap.getIsReadyWrite().get() ) {
                if (!socketChannel.isConnected()) {
                    return;
                }
            }
            bufferWrap.getIsReadyWrite().set(false);
            writeBuffer.put(ByteBuffer.wrap(bytesToWrite));
            if (bytesToWrite.length != 0) {
                if ((bytesToWrite[bytesToWrite.length - 1]) != '\n') {
                    writeBuffer.put((byte) '\n');
                }
            }

            writeBuffer.flip();

            try {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            } catch (ClosedChannelException e) {
                log.log(Level.INFO, "Connection is close");
            }

        }
    }

    public Map<SocketChannel, ByteBufferWrap> getChannels() {
        return channels;
    }
}
