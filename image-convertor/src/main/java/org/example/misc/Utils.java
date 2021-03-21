package org.example.misc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.example.model.CustomImage;
import org.example.model.Pixel;

public class Utils {
    public static byte[] longToByteArraySize4(long value) {
        byte[] arraySize8 = ByteBuffer.allocate(8).putLong(value).array();
        return Arrays.copyOfRange(arraySize8, 4, 8);
    }

    public static long byteArrayToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}
