package games.tictactoe;

import command.Command;
import command.CommandExecutor;
import games.Game;

public class TicTacToeExecutor implements CommandExecutor {

    /* ===================== FIRST COMMAND ======================= */
    private static final String INVITE = "create";
    private static final String JOIN = "join";
    private static final String PRINT = "print";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    /* ===================== INGAME COMMAND ====================== */
    private static final String HIT = "hit";
    /* =========================================================== */
    private static final String UNKNOWN_COMMAND = "Unknown command";
    public static final String DISCONNECTED = "Disconnected from the server";
    public static String helpMessage = "PRINTING HELP MESSAGE!";
    Game game;

    public TicTacToeExecutor(Game game) {
        this.game = game;
    }
    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case INVITE -> invite(cmd);
            case HIT -> hit(cmd);
            case EXIT -> exit();
            case PRINT -> print(cmd);
            default -> UNKNOWN_COMMAND;
        };
    }

    private String invite(Command cmd) {
        return null;
    }
    // print
    public String print(Command cmd) {
        return null;
    }
    // The command looks like this: "hit row-column"
    public String hit(Command cmd) {
        return null;
    }
    public String join(Command cmd) {
        return null;
    }
    public String exit() {
        return DISCONNECTED;
    }

    public String help() {
        return helpMessage;
    }
}
