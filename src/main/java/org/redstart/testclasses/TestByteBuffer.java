package org.redstart.testclasses;

import java.nio.ByteBuffer;

public class TestByteBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1000);

        byte[] bytes = new byte[]{123, 25, 51, 65};

        buffer.put(ByteBuffer.wrap(bytes));

        if (bytes.length != 0) {
            if ((bytes[bytes.length - 1]) != 10) {
                buffer.put((byte) 10);
            }
        }

        buffer.flip();
    }
}
