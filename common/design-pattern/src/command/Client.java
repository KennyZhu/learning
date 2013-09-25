package command;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午6:40
 */
public class Client {
    private List<Command> commandList;

    public Client() {
        this.commandList = new ArrayList<Command>();
    }

    public void addCommand(Command command) {
        this.commandList.add(command);
    }

    public void sendToServer() {
        for (Command command : commandList) {
            command.execute();
        }
    }
}
