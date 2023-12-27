package org.packman.server;

import lombok.RequiredArgsConstructor;
import org.packman.server.socket.ServerSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.packman.server.socket.ServerSocket.listen;
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        listen();
    }
}