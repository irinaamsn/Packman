package org.packman.server;

import org.packman.server.socket.ServerSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.packman.server.socket.ServerSocket.listen;
@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
        listen();
    }
}