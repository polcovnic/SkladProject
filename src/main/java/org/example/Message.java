package org.example;

import lombok.Data;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Base64;

@Data
public class Message {
    private Integer cType;
    private Integer bUserId;
    private String message;

    public static final int BYTES_WITHOUT_MESSAGE = Integer.BYTES * 2;
    public static final int MAX_MESSAGE_SIZE = 255;
    public static final int BYTES_MAX_SIZE = BYTES_WITHOUT_MESSAGE + MAX_MESSAGE_SIZE;

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "ADBSJHJS12547896".getBytes();
    private static final Key key = new SecretKeySpec(keyValue, ALGORITHM);

    public Message() {}

    public Message(int cType, int bUserId, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public byte[] packMessage() {
        return ByteBuffer.allocate(getMessageBytesLength())
                .putInt(cType)
                .putInt(bUserId)
                .put(message.getBytes())
                .array();
    }

    public int getMessageBytesLength() {
        return BYTES_WITHOUT_MESSAGE + getMessageBytes();
    }

    public int getMessageBytes() {
        return message.getBytes().length;
    }

    @SneakyThrows
    public void encode() {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = cipher.doFinal(message.getBytes());
        message = Base64.getEncoder().encodeToString(encryptedValue);
    }

    @SneakyThrows
    public void decode() {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(message.getBytes());
        message = new String(cipher.doFinal(decodedValue));
    }
}
