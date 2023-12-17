package command;

import java.util.Arrays;

public record Command(String player, String game, String[] arguments) {

    public String command() {
        return Arrays.stream(arguments).toList().get(0);
    }
}