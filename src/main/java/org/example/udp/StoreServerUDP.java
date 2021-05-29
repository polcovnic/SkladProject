package org.example.udp;

import org.example.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

//acts as EchoServer
public class StoreServerUDP extends Thread{
    private DatagramSocket socket;
    private byte[] buf = new byte[Packet.packetMaxSize];

    public StoreServerUDP(int port) throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println("Start UDPServer on port " + port);
    }

    public void close() {
        socket.close();
    }

    public void run() {
        try {
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                socket.receive(datagramPacket);

                Packet packet = new Packet(buf);
                System.out.println(packet.getBMsq().getMessage());

                InetAddress address = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                datagramPacket = new DatagramPacket(buf, buf.length, address, port);
                socket.send(datagramPacket);
            }
        } catch (IOException e) {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
