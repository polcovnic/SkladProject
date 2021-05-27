package org.example;

import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable {
    private final BlockingQueue<Message> queueForProducing;
    private final BlockingQueue<Message> queueForConsuming;

    private final Message poisonPillForConsuming;
    private final Message poisonPillForProducing;

    public Processor(BlockingQueue<Message> queueForProducing, BlockingQueue<Message> queueForConsuming, Message poisonPillForConsuming, Message poisonPillForProducing) {
        this.queueForProducing = queueForProducing;
        this.queueForConsuming = queueForConsuming;
        this.poisonPillForConsuming = poisonPillForConsuming;
        this.poisonPillForProducing = poisonPillForProducing;
    }


    Message process(Message message) {
        //fake implementation of processing
        return new Message(CommandTypeEncoder.PRODUCT_CREATE, 1, "Ok");
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

                Message reply = process(message);
                queueForProducing.put(reply);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
