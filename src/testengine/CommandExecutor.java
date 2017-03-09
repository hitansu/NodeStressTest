package testengine;

import java.util.List;

public interface CommandExecutor {
	public boolean execute(String command);
	public void executeForResult(String command, List<String> result);
}
