package command;

import java.util.Arrays;
import java.util.List;

public class CommandCreator {
    public static Command newCommand(String clientInput) {
        List<String> tokens = Arrays.stream(clientInput.split(" ")).toList();
        return new Command(tokens.get(0), tokens.get(1), tokens.subList(2, tokens.size()).toArray(String[]::new));
    }
}
