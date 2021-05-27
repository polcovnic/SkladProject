package org.example;

import static org.junit.Assert.assertTrue;

import com.google.common.primitives.UnsignedLong;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void testMultithreadedMessageSending() {
        Client client = new Client();
        client.listen();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(client::send).start();
        }

        List<String> responses = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            String response = client.receive();
            if (response != null) {
                responses.add(response);
            }
        }

        client.close();
        Assert.assertEquals(responses.size(), numberOfThreads);
    }
}
