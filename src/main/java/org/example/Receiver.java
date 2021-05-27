package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Receiver implements Runnable{
    private final BlockingQueue<byte[]> queueForProducing;
    private final BlockingQueue<byte[]> queueForConsuming;

    private final byte[] poisonPillForConsuming;
    private final byte[] poisonPillForProducing;

    public Receiver(BlockingQueue<byte[]> queueForConsuming, BlockingQueue<byte[]> queueForProducing, byte[] poisonPillForConsuming, byte[] poisonPillForProducing) {
        this.queueForProducing = queueForProducing;
        this.queueForConsuming = queueForConsuming;
        this.poisonPillForConsuming = poisonPillForConsuming;
        this.poisonPillForProducing = poisonPillForProducing;
    }

    byte[] receiveMessage() throws InterruptedException {
        return queueForConsuming.take();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] message = receiveMessage();
                if (Arrays.equals(message, poisonPillForConsuming)) {
                    queueForProducing.put(poisonPillForProducing);
                    return;
                }

                queueForProducing.put(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
