package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Sender implements Runnable{
    private final BlockingQueue<byte[]> queueForProducing;
    private final BlockingQueue<byte[]> queueForConsuming;

    private final byte[] poisonPillForConsuming;
    private final byte[] poisonPillForProducing;

    public Sender(BlockingQueue<byte[]> queueForConsuming, BlockingQueue<byte[]> queueForProducing, byte[] poisonPillForConsuming, byte[] poisonPillForProducing) {
        this.queueForProducing = queueForProducing;
        this.queueForConsuming = queueForConsuming;
        this.poisonPillForConsuming = poisonPillForConsuming;
        this.poisonPillForProducing = poisonPillForProducing;
    }

    void sendMessage(byte[] message) throws InterruptedException {
        queueForProducing.put(message);
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

                sendMessage(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}