package org.packman.server.socket;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.packman.server.logic.Command;
import org.packman.server.logic.GameLogic;
import org.packman.server.logic.GameLogicImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.packman.server.utils.ParseUtil.parseToArray;

@AllArgsConstructor
public class ServerSocket {
    private static final int PORT = 2020;
    private static final Logger logger = LogManager.getLogger(ServerSocket.class);

    public static void listen() {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            GameLogic gameLogic  = new GameLogicImpl();
            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("Получен запрос от клиента: " + request);
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                String clientPort = String.valueOf(clientSocket.getPort());
                String[] command = parseToArray(request);
                String response = gameLogic.processing(clientIP, clientPort, Command.valueOf(command[0]));
                writer.println(response);
                System.out.println("Отправлен ответ клиенту: " +response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
