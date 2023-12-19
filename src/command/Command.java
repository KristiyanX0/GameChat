package command;

import java.util.Arrays;

public record Command(String player, String game, String[] arguments) {
    public String command(int index) {
        return arguments[index];
    }

    @Override
    public String toString() {
        return "Command{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}