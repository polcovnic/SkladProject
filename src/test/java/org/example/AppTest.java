package org.example;

import static org.junit.Assert.assertTrue;

import com.google.common.primitives.UnsignedLong;
import org.example.tcp.StoreClientTCP;
import org.example.tcp.StoreServerTCP;
import org.example.udp.StoreClientUDP;
import org.example.udp.StoreServerUDP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private StoreServerTCP serverTCP;
    private StoreServerUDP serverUDP;

    @Before
    public void startServers() {
        new Thread(() -> {
            serverTCP = new StoreServerTCP();
            try {
                serverTCP.listen(5001);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            serverUDP = new StoreServerUDP(5007);
            serverUDP.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTCPConnectionWithMultipleClients() {
        StoreClientTCP client1 = new StoreClientTCP();
        StoreClientTCP client2 = new StoreClientTCP();
        StoreClientTCP client3 = new StoreClientTCP();

        client1.startConnection("127.0.0.1", 5001);
        client2.startConnection("127.0.0.1", 5001);
        client3.startConnection("127.0.0.1", 5001);

        Packet packet1 = new Packet(Integer.valueOf(1).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageTCP"));
        Packet packet2 = new Packet(Integer.valueOf(2).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageTCP"));
        Packet packet3 = new Packet(Integer.valueOf(3).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageTCP"));

        String response1 = client1.send(packet1).getBMsq().getMessage();
        String response2 = client2.send(packet2).getBMsq().getMessage();
        String response3 = client3.send(packet3).getBMsq().getMessage();

        client1.stopConnection();
        client2.stopConnection();
        client3.stopConnection();

        Assert.assertArrayEquals(new String[]{"messageTCP", "messageTCP", "messageTCP"}, new String[]{response1, response2, response3});
    }

    @Test
    public void testUDPConnectionWithMultipleClients() {
        StoreClientUDP client1 = new StoreClientUDP();
        StoreClientUDP client2 = new StoreClientUDP();
        StoreClientUDP client3 = new StoreClientUDP();

        try {
            client1.connect("localhost", 5007);
            client2.connect("localhost", 5007);
            client3.connect("localhost", 5007);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Packet packet1 = new Packet(Integer.valueOf(1).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageUDP"));
        Packet packet2 = new Packet(Integer.valueOf(2).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageUDP"));
        Packet packet3 = new Packet(Integer.valueOf(3).byteValue(), UnsignedLong.valueOf(5), new Message(CommandTypeEncoder.PRODUCT_CREATE, 5, "messageUDP"));

        String response1 = client1.send(packet1).getBMsq().getMessage();
        String response2 = client2.send(packet2).getBMsq().getMessage();
        String response3 = client3.send(packet3).getBMsq().getMessage();

        client1.close();
        client2.close();
        client3.close();

        Assert.assertArrayEquals(new String[]{"messageUDP", "messageUDP", "messageUDP"}, new String[]{response1, response2, response3});
    }

    @After
    public void stopServers() {
        serverTCP.close();
        serverUDP.close();
    }

}
