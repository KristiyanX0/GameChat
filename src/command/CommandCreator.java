package command;

import java.util.Arrays;
import java.util.List;

public class CommandCreator {
    public static Command newCommand(String clientInput) {
        List<String> tokens = Arrays.stream(clientInput.split(" ")).toList();
        return new Command(tokens.get(2), tokens.get(3), tokens.subList(4, tokens.size()).toArray(String[]::new));
    }
}
