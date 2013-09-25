package command;

/**
 * User: Yanlong
 * Date: 12-12-7
 * Time: 下午4:05
 */
public class FirstCommandImpl extends Command {

    public FirstCommandImpl(Executor executor) {
        super(executor);
    }

    @Override
    public void execute() {
        this.getExecutor().method1();
    }
}

