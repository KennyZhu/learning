package command;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午4:06
 */
public class Server {
    private List<Command> commands = new ArrayList<Command>();

    public void receiveCommand(Command command) {
        commands.add(command);
    }

    public void service() {
        for (Command command : commands) {
            command.execute();
        }
    }
}
