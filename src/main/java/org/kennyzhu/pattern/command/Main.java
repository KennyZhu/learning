package org.kennyzhu.pattern.command;

/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午4:06
 * DESC: 命令模式
 */
public class Main {

    public static void main(String[] args) {
        Executor executor = new Executor();
        Command command1 = new FirstCommandImpl(executor);
        Command command2 = new SecondCommandImpl(executor);
        Client client = new Client();
        client.addCommand(command1);
        client.addCommand(command2);
        client.sendToServer();
    }
}
