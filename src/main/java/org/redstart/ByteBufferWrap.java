package org.redstart;

public class ByteBufferWrap {

    public byte[] bytes;

    public long timeCreation;

    public ByteBufferWrap(byte[] bytes) {
        this.bytes = bytes;
        timeCreation = System.currentTimeMillis();
    }
}
