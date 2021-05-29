package org.example.tcp;

import lombok.SneakyThrows;
import org.example.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//acts as EchoServer
public class StoreServerTCP {
    private ServerSocket serverSocket;

    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Start TCPServer on port " + port);
        try {
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();
        } catch (IOException e) {
            close();
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private OutputStream out;
        private InputStream in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @SneakyThrows
        public void run() {
            try {
                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                while (true) {
                    byte[] maxPacketBuffer = new byte[Packet.packetMaxSize];
                    in.read(maxPacketBuffer);

                    Packet packet = new Packet(maxPacketBuffer);
                    System.out.println(packet.getBMsq().getMessage());

                    out.write(maxPacketBuffer);
                    out.flush();
                }
            } catch (Exception e) {
                in.close();
                out.close();
                clientSocket.close();

                return;
            }
        }

    }
}
