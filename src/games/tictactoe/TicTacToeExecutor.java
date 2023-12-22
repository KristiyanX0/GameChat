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

    /* ===================== RESPONSES ====================== */
    private static final String END = "GAME HAS ENDED";
    private static final String UNKNOWN_COMMAND = "Unknown command";

    private static HashMap<String,TicTacToe> games = new HashMap<>();;

    private static Response newGame(Command command, String currentPlayer) {
        if (!games.containsKey(command.game())) {
            return new Response("Game doesn't exist!", List.of(currentPlayer));
        }

        String response =  (!games.containsKey(command.game()) ?
                "New game called " + command.game() + " was created!" :
                command.game() + " already exist!");
        games.putIfAbsent(command.game(), new TicTacToe(currentPlayer, command.command(1)));
        return new Response(response, List.of(currentPlayer, command.command(1)));
    }

    public static boolean ifGameExist(Command command) {
        return games.containsKey(command.game());
    }

    public static Response execute(Command cmd, String currentPlayer) {
        return switch (cmd.command(0)) {
            case CREATE -> newGame(cmd, currentPlayer);
            case SIGN -> mySign(cmd, currentPlayer);
            case HIT -> hit(cmd, currentPlayer);
            case EXIT -> exit(cmd);
            case PRINT -> print(cmd, List.of(currentPlayer), "");
            case HELP -> help(cmd,currentPlayer);
            case LIST -> listOfActiveGames(cmd, currentPlayer);
            default -> new Response(UNKNOWN_COMMAND, List.of(currentPlayer));
        };
    }

    private static Response mySign(Command cmd, String currentPlayer) {
        return new Response(games.get(cmd.game()).getPlayerSign(currentPlayer).toString(), List.of(currentPlayer));
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
        if (!games.get(cmd.game()).hit(currentPlayer, Integer.parseInt(cmd.command(1)), Integer.parseInt(cmd.command(2)))) {
            if (games.get(cmd.game()).hasEnded()) {
                add = System.lineSeparator() + "Winner: " + games.get(cmd.game()).getWinner();
                add += System.lineSeparator() + exit(cmd).message();
            }
        }
        return print(cmd, List.of(currentPlayer, cmd.command(1)), add);
    }
    public static Response exit(Command command) {
        return new Response(END, List.of(games.get(command.game()).player1.getSecond(), games.get(command.game()).player2.getSecond()));
    }

    public static Response help(Command command, String currentPlayer) {
        String response = "The commands are" + System.lineSeparator() +
                "*player name* *game name* hit x y - you place your marker on a square where x and y are the coordinates!" + System.lineSeparator() +
                "*player name* *game name* create - create a new game if it doesn't exist or this one has ended!" + System.lineSeparator() +
                "*player name* *game name* exit - exit from a game and automatically deletes it!" + System.lineSeparator() +
                "*player name* *game name* print - prints the board you are playing on!" + System.lineSeparator();
        return new Response(response, List.of(currentPlayer));
    }

    public static Response listOfActiveGames(Command command, String currentPlayer) {
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, TicTacToe> game : games.entrySet()) {
            response.append(game.getKey()).append(" -> [ ").
                    append(game.getValue().player1.getSecond()).append(" - ").
                    append(game.getValue().player2.getSecond()).append(" ]");
        }
        return new Response(response.toString(), List.of(currentPlayer));
    }
}

//-> john game0 create
//-> john game0 print
//
