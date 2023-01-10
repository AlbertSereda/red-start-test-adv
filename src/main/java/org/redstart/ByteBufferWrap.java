package org.redstart;

import java.nio.ByteBuffer;

public class ByteBufferWrap {

    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

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
}
