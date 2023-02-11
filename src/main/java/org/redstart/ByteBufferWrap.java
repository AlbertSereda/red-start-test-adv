package org.redstart;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class ByteBufferWrap {

    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    private AtomicBoolean isReadyWrite = new AtomicBoolean(true);

    public ByteBufferWrap(ByteBuffer readBuffer, ByteBuffer writeBuffer) {
        this.readBuffer = readBuffer;
        this.writeBuffer = writeBuffer;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public AtomicBoolean getIsReadyWrite() {
        return isReadyWrite;
    }
}
