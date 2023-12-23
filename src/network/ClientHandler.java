package network;

import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import games.tictactoe.TicTacToeExecutor;
import util.Response;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static network.TypeNotification.*;

public class ClientHandler implements Runnable {
    // SIZE MAX IS CURRENTLY 2 BUT IT CAN BE EASILY SCALED
    private static final int MAX_SIZE_CLIENTS = 4;
    private final String inGameResponse = "# ->" + System.lineSeparator();
    private static HashMap<String, ClientHandler> clientHandlers = new HashMap<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public static boolean createAnInstance(Socket socket) {
        if (!ClientHandler.isFull()) {
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread thread = new Thread(clientHandler);
            thread.start();
            return true;
        } else {
            System.out.println("NO MORE SPACE!");
        }
        return false;
    }
    private ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.putIfAbsent(clientUsername, this);
            sendMessage("SERVER: " + clientUsername + " has joined!", OTHER_PLAYERS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessage(String message, TypeNotification typeNotification, Set<String> usernames) {
        for (Map.Entry<String, ClientHandler> clientHandler : clientHandlers.entrySet()) {
            if (usernames.contains(clientHandler.getKey()) && typeNotification.equals(OTHER_PLAYERS)) {
                sendMessage(message, PLAYER, clientHandler.getKey());
            }
        }
    }
    private void sendMessage(String message, TypeNotification typeNotification, String username) {
        if (typeNotification.equals(PLAYER) && !clientHandlers.get(username).equals(clientUsername)) {
            try {
                clientHandlers.get(username).bufferedWriter.write(message);
                clientHandlers.get(username).bufferedWriter.newLine();
                clientHandlers.get(username).bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    private void sendMessage(String message, TypeNotification typeNotification) {
        for (Map.Entry<String, ClientHandler> clientHandler : clientHandlers.entrySet()) {
            if (typeNotification.equals(BROADCAST) ||
                    (!clientHandler.getKey().equals(clientUsername) && typeNotification.equals(OTHER_PLAYERS))) {
                sendMessage(message, PLAYER, clientHandler.getKey());
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        sendMessage("SERVER: " + clientUsername + "has left!", OTHER_PLAYERS);
    }

    public static boolean isFull() {
        return clientHandlers.size() >= MAX_SIZE_CLIENTS;
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (CommandExecutor.isCommand(clientHandlers.keySet(), messageFromClient)) {
                    Command command = CommandCreator.newCommand(messageFromClient);
                    System.out.println(command);
                    Response response = TicTacToeExecutor.execute(command, clientUsername, clientHandlers.keySet().stream().toList());
                    sendMessage(inGameResponse + response.message(), OTHER_PLAYERS, Set.copyOf(response.client()));
                } else {
                    sendMessage(messageFromClient, OTHER_PLAYERS);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedWriter != null) {
                bufferedReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
