package games.tictactoe;

import command.Command;
import util.Matrix;
import util.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TicTacToeExecutor {
    /* ================== IN GAME COMMANDS ================== */
    private static final String HELP = "help";
    private static final String CREATE = "create";
    private static final String EXIT = "exit";
    private static final String HIT = "hit";
    private static final String LIST = "list";
    private static final String SIGN = "sign";
    private static final String PRINT = "print";
    private static final String DELETE = "delete";

    /* ===================== RESPONSES ====================== */
    private static final String END = "GAME HAS ENDED";
    private static final String UNKNOWN_COMMAND = "Unknown command";
    private static HashMap<String,TicTacToe> games = new HashMap<>();

    public static Response execute(Command cmd, String currentPlayer, List<String> clients) {
        return switch (cmd.command(0)) {
            case CREATE -> newGame(cmd, currentPlayer, clients);
            case SIGN -> mySign(cmd, currentPlayer);
            case HIT -> hit(cmd, currentPlayer);
            case EXIT -> exit(cmd);
            case PRINT -> print(cmd, List.of(currentPlayer), "");
            case HELP -> help(cmd,currentPlayer);
            case LIST -> listOfActiveGames(cmd, currentPlayer);
            case DELETE -> delete(cmd, currentPlayer);
            default -> new Response(UNKNOWN_COMMAND, List.of(currentPlayer));
        };
    }

    private static Response newGame(Command command, String currentPlayer, List<String> clients) {
        String response =  (!games.containsKey(command.game()) ?
                "New game called " + command.game() + " was created!" :
                command.game() + " already exist!");
        if (currentPlayer.equals(command.command(1))) {
            return new Response("Can't use your name!", List.of(currentPlayer));
        } else if (!clients.contains(command.command(1))) {
            return new Response("This client doesn't exist", List.of(currentPlayer));
        } else {
            games.putIfAbsent(command.game(), new TicTacToe(currentPlayer, command.command(1)));
        }
        return new Response(response, List.of(currentPlayer, command.command(1)));
    }

    public static boolean ifGameExist(Command command) {
        return games.containsKey(command.game());
    }

    private static Response delete(Command cmd, String currentPlayer) {
        if (!games.containsKey(cmd.game())) {
            return new Response("Game doesn't exist!", List.of(currentPlayer));
        }
        String player1 = games.get(cmd.game()).player1.second();
        String player2 = games.get(cmd.game()).player2.second();
        if (player1.equals(currentPlayer) || player2.equals(currentPlayer)) {
            games.remove(cmd.game());
        }
        return new Response("Game deleted", List.of(player1, player2));
    }

    private static Response mySign(Command cmd, String currentPlayer) {
        if (!games.containsKey(cmd.game())) {
            return new Response("Game doesn't exist!", List.of(currentPlayer));
        }
        String player1 = games.get(cmd.game()).player1.second();
        String player2 = games.get(cmd.game()).player2.second();
        if (player1.equals(currentPlayer) || player2.equals(currentPlayer)) {
            return new Response(games.get(cmd.game()).getPlayerSign(currentPlayer).toString(), List.of(currentPlayer));
        }
        return new Response("", List.of(currentPlayer));
    }

    // print
    public static Response print(Command cmd, List<String> players, String addMessage) {
        if (!games.containsKey(cmd.game())) {
            return new Response("Game doesn't exist!", players);
        }
        Matrix matrix = games.get(cmd.game()).getMatrix();
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.size(); ++j) {
                response.append(matrix.get(i, j).toString()).append(" | ");
            }
            response.append(System.lineSeparator());
        }
        return new Response(response.toString() + addMessage, players);
    }
    // The command looks like this: "hit row column"
    public static Response hit(Command cmd, String currentPlayer) {
        if (!games.containsKey(cmd.game())) {
            return new Response("Game doesn't exist!", List.of(currentPlayer));
        }
        String add = "";

        if (games.get(cmd.game()).turn(currentPlayer)) {
            int row = Integer.parseInt(cmd.command(1));
            int col = Integer.parseInt(cmd.command(2));
            if (games.get(cmd.game()).getMatrix().validArguments(row, col)) {
                if (!games.get(cmd.game()).hit(currentPlayer, row, col)) {
                    if (games.get(cmd.game()).hasEnded()) {
                        add = System.lineSeparator() + "Winner: " + games.get(cmd.game()).getWinner();
                        add += System.lineSeparator() + exit(cmd).message();
                    }
                }
            } else {
                return new Response("Out of boundaries!", List.of(currentPlayer));
            }
        } else {
            return new Response("Not you turn!", List.of(currentPlayer));
        }
        return print(cmd, List.of(currentPlayer, cmd.command(1)), add);
    }
    public static Response exit(Command command) {
        return new Response(END, List.of(games.get(command.game()).player1.second(), games.get(command.game()).player2.second()));
    }

    public static Response help(Command command, String currentPlayer) {
        String response = "The commands are" + System.lineSeparator() +
                "*game name* hit x y - you place your marker on a square where x and y are the coordinates!" + System.lineSeparator() +
                "*game name* create *player name*  - create a new game if it doesn't exist or this one has ended!" + System.lineSeparator() +
                "*game name* delete - deletes the game!" + System.lineSeparator() +
                "*game name* print - prints the board you are playing on!" + System.lineSeparator();
        return new Response(response, List.of(currentPlayer));
    }

    public static Response listOfActiveGames(Command command, String currentPlayer) {
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, TicTacToe> game : games.entrySet()) {
            response.append(game.getKey()).append(" -> [ ").
                    append(game.getValue().player1.second()).append(" - ").
                    append(game.getValue().player2.second()).append(" ]");
        }
        return new Response(response.toString(), List.of(currentPlayer));
    }
}