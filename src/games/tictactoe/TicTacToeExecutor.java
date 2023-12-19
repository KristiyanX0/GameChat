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
    private static final String END = "ENDED THE GAME";
    private static final String UNKNOWN_COMMAND = "Unknown command";


    private static HashMap<String,TicTacToe> games = new HashMap<>();;

    private static Response newGame(Command command, String currentPlayer) {
        String response =  (!games.containsKey(command.game()) ?
                "New game called " + command.game() + " was created!" :
                command.game() + " already exist!");
        games.putIfAbsent(command.game(), new TicTacToe(currentPlayer, command.player()));
        return new Response(response, List.of(currentPlayer, command.player()));
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
            case PRINT -> print(cmd, currentPlayer);
            case HELP -> help(cmd);
            case LIST -> listOfActiveGames(cmd, currentPlayer);
            default -> new Response(UNKNOWN_COMMAND, List.of(currentPlayer));
        };
    }

    private static Response mySign(Command cmd, String currentPlayer) {
        return new Response(games.get(cmd.game()).getPlayerSign(currentPlayer).toString(), List.of(currentPlayer));
    }

    // print
    public static Response print(Command cmd, String currentPlayer) {
        Matrix matrix = games.get(cmd.game()).getMatrix();
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.size(); ++j) {
                response.append(matrix.get(i, j).toString()).append(" | ");
            }
            response.append(System.lineSeparator());
        }
        return new Response(response.toString(), List.of(currentPlayer));
    }
    // The command looks like this: "hit row-column"
    public static Response hit(Command cmd, String currentPlayer) {
        games.get(cmd.game()).put(currentPlayer, Integer.parseInt(cmd.command(1)), Integer.parseInt(cmd.command(2)));
        return print(cmd, currentPlayer);
    }
    public static Response exit(Command command) {
        return new Response(END, List.of(games.get(command.game()).player1.getSecond(), games.get(command.game()).player2.getSecond()));
    }

    public static Response help(Command command) {
        String response = "The commands are" + System.lineSeparator() +
                "*player name* *game name* hit x y - you place your marker on a square where x and y are the coordinates!" + System.lineSeparator() +
                "*player name* *game name* create - create a new game if it doesn't exist or this one has ended!" + System.lineSeparator() +
                "*player name* *game name* exit - exit from a game and automatically deletes it!" + System.lineSeparator() +
                "*player name* *game name* print - prints the board you are playing on!" + System.lineSeparator();
        return new Response(response, List.of(games.get(command.game()).player1.getSecond()));
    }

    public static Response listOfActiveGames(Command command, String currentPlayer) {
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, TicTacToe> game : games.entrySet()) {
            response.append(game.getKey()).append(" -> [ ").append(game.getValue().player1.getSecond()).append(game.getValue().player2.getSecond()).append(" ]");
        }
        return new Response(response.toString(), List.of(currentPlayer));
    }
}

//-> john game0 create
//-> john game0 print
//
