package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Utils {
    public static String GenerateRandomString(long len) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }

    public static int byteArrayToInt(byte[] value) {
        return ((value[0] & 0xFF) << 24) |
                ((value[1] & 0xFF) << 16) |
                ((value[2] & 0xFF) << 8)  |
                (value[3] & 0xFF);
    }

    public static boolean readBytesFromInputStream(InputStream in) throws IOException {
        byte[] sizeInBytes = in.readNBytes(4);
        if (sizeInBytes.length < 4)
            return false;
        int size = Utils.byteArrayToInt(sizeInBytes);
        byte[] buffer = new byte[4096];

        while (size > 0) {
            int toRead = Math.min(buffer.length, size);
            // в данном случае у нас нет цели что-то делать с полученным сообщением (его даже не надо выводить)
            // поэтому мы просто считываем доступные байты во временный буфер
            // при надобности можно было бы записывать полученные данные, например, в БД или на диск
            int n = in.read(buffer, 0, toRead);
            if (n == -1) throw new IOException("Unexpected end of stream");
            size -= n;
        }
        return true;
    }

}
