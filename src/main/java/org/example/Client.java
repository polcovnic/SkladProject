package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Client {
    BlockingQueue<Message> basicEncryptorQueue;
    BlockingQueue<byte[]> encryptorSenderQueue;
    BlockingQueue<byte[]> senderReceiverQueue;
    BlockingQueue<byte[]> receiverDecryptorQueue;
    BlockingQueue<Message> decryptorBasicQueue;

    Message messagePoisonPill;
    byte[] bytePoisonPill;

    int numberOfThreads;

    public Client() {
        basicEncryptorQueue = new LinkedBlockingQueue<>();
        encryptorSenderQueue = new LinkedBlockingQueue<>();
        senderReceiverQueue = new LinkedBlockingQueue<>();
        receiverDecryptorQueue = new LinkedBlockingQueue<>();
        decryptorBasicQueue = new LinkedBlockingQueue<>();

        messagePoisonPill = new Message(-1, -1, "poison pill");
        bytePoisonPill = new byte[]{-1};

    }

    void start() {
        numberOfThreads = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(new Encryptor(basicEncryptorQueue, encryptorSenderQueue, messagePoisonPill, bytePoisonPill)).start();
            new Thread(new Sender(encryptorSenderQueue, senderReceiverQueue, bytePoisonPill, bytePoisonPill)).start();
            new Thread(new Receiver(senderReceiverQueue, receiverDecryptorQueue, bytePoisonPill, bytePoisonPill)).start();
            new Thread(new Decryptor(receiverDecryptorQueue, decryptorBasicQueue, bytePoisonPill, messagePoisonPill)).start();
        }
    }

    void close() {
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                basicEncryptorQueue.put(messagePoisonPill);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void send() {
        Message messageForEncryptor = new Message(CommandTypeEncoder.PRODUCT_CREATE, 1, "message");
        try {
            basicEncryptorQueue.put(messageForEncryptor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String receive() {
        String response = null;
        try {
            response = decryptorBasicQueue.take().getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }
}
