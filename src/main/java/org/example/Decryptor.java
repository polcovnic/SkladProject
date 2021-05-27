package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Decryptor implements Runnable {
    private final BlockingQueue<Message> queueForProducing;
    private final BlockingQueue<byte[]> queueForConsuming;

    private final byte[] poisonPillForConsuming;
    private final Message poisonPillForProducing;

    public Decryptor(BlockingQueue<byte[]> queueForConsuming, BlockingQueue<Message> queueForProducing, byte[] poisonPillForConsuming, Message poisonPillForProducing) {
        this.queueForProducing = queueForProducing;
        this.queueForConsuming = queueForConsuming;
        this.poisonPillForConsuming = poisonPillForConsuming;
        this.poisonPillForProducing = poisonPillForProducing;
    }

    Message decrypt(byte[] message) throws Exception {
        return new Packet(message).getBMsq();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] message = queueForConsuming.take();
                if (Arrays.equals(message, poisonPillForConsuming)) {
                    queueForProducing.put(poisonPillForProducing);
                    return;
                }

                Message decryptedMessage = decrypt(message);
                queueForProducing.put(decryptedMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
