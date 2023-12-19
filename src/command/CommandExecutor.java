package command;

import java.util.Set;

public interface CommandExecutor {
    public static boolean isCommand(Set<String> names, String message) {
        if (message == null) {
            return false;
        }
        for (String name : names) {
            if (message.startsWith(name + ": ->")) {
                return true;
            }
        }
        return false;
    }
}
