package org.example;

import com.google.common.primitives.UnsignedLong;

import java.util.concurrent.BlockingQueue;

public class Encryptor implements Runnable {
    private final BlockingQueue<byte[]> queueForProducing;
    private final BlockingQueue<Message> queueForConsuming;

    private final Message poisonPillForConsuming;
    private final byte[] poisonPillForProducing;

    public Encryptor(BlockingQueue<Message> queueForConsuming, BlockingQueue<byte[]> queueForProducing, Message poisonPillForConsuming, byte[] poisonPillForProducing) {
        this.queueForProducing = queueForProducing;
        this.queueForConsuming = queueForConsuming;

        this.poisonPillForConsuming = poisonPillForConsuming;
        this.poisonPillForProducing = poisonPillForProducing;
    }

    byte[] encrypt(Message message) {
        //fake implementation of encryption
        return new Packet((byte) 5, UnsignedLong.valueOf(5), message).toPacket();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = queueForConsuming.take();
                if (message.equals(poisonPillForConsuming)) {
                    queueForProducing.put(poisonPillForProducing);
                    return;
                }

                byte[] encryptedMessage = encrypt(message);
                queueForProducing.put(encryptedMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
