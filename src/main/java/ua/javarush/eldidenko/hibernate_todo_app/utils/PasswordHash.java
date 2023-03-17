package ua.javarush.eldidenko.hibernate_todo_app.utils;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class PasswordHash {
    private static final String HASH_SALT = "njcj@MKW9@%</C";

    @SneakyThrows
    public static String hash(char[] password) {
        CharBuffer charBuffer = CharBuffer.wrap(password);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] passwordAsBytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(HASH_SALT.getBytes());
        byte[] bytes = messageDigest.digest(passwordAsBytes);

        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();
    }
}
