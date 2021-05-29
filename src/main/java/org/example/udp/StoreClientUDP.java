package org.example.udp;

import org.example.Packet;

import java.io.IOException;
import java.net.*;

public class StoreClientUDP {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;


    public StoreClientUDP() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void connect(String ip, int port) throws UnknownHostException {
        address = InetAddress.getByName(ip);
        this.port = port;
    }

    public Packet send(Packet packet) {
        Packet receivedPacket = null;
        try {
            byte[] buf = packet.toPacket();
            DatagramPacket datagramPacket
                    = new DatagramPacket(buf, buf.length, address, port);
            socket.send(datagramPacket);

            byte[] response = new byte[Packet.packetMaxSize];
            datagramPacket = new DatagramPacket(response, response.length);
            socket.receive(datagramPacket);
            receivedPacket = new Packet(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedPacket;
    }

    public void close() {
        socket.close();
    }
}
