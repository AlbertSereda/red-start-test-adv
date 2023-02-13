package org.redstart;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {

    private static final int READ_BUFFER_CAPACITY = 1000;

    private static final int WRITE_BUFFER_CAPACITY = 3000;

    private final SocketChannel socketChannel;

    private final ByteBuffer writeBuffer;

    private final ByteBuffer readBuffer;

    private final Queue<byte[]> writeToSocketQueue;

    public final Queue<ByteBufferWrap> bufferWraps;
    public  boolean isWritable = false;
    public  boolean isReadable = false;

    public SocketClient(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_CAPACITY);
        this.readBuffer = ByteBuffer.allocate(READ_BUFFER_CAPACITY);
        this.writeToSocketQueue = new ConcurrentLinkedQueue<>();
        this.bufferWraps = new LinkedBlockingQueue<>();
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public Queue<byte[]> getWriteToSocketQueue() {
        return writeToSocketQueue;
    }

    public void addToWriteQueue(byte[] bytes) {
        //writeToSocketQueue.add(bytes);
        bufferWraps.add(new ByteBufferWrap(bytes));
    }
}
