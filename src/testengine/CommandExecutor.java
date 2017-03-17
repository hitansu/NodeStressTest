package testengine;

import java.util.List;

/**
 * Interface for implementing command remotely
 * @author jenah
 *
 */
public interface CommandExecutor {
	/**
	 * Execute a command
	 * @param command
	 * @return
	 */
	public boolean execute(String command);
	
	/**
	 * Execute command for results
	 * @param command
	 * @param result
	 */
	public void executeForResult(String command, List<String> result);
}
