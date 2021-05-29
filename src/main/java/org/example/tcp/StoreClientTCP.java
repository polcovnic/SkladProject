package org.example.tcp;

import lombok.SneakyThrows;
import org.example.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class StoreClientTCP {
    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = clientSocket.getOutputStream();
            in = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet send(Packet packet) {
        byte[] response = new byte[Packet.packetMaxSize];
        try {
            out.write(packet.toPacket());
            in.read(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Packet responsePacket = null;
        try {
            responsePacket = new Packet(response);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return responsePacket;
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
