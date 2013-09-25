package org.kennyzhu.pattern.command;
/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午4:02
 */
public abstract class Command {
    private Executor executor;

    public Command(Executor executor) {
        this.executor = executor;
    }

    public abstract void execute();

    public Executor getExecutor() {
        return executor;
    }
}
