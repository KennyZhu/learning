package org.kennyzhu.pattern.command;
/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午4:05
 */
public class SecondCommandImpl extends Command {
    public SecondCommandImpl(Executor executor) {
        super(executor);
    }

    public void execute() {
        this.getExecutor().method2();
    }
}
