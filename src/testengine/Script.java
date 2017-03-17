package testengine;

/**
 * Script interface for Node start,stop
 * @author jenah
 *
 */
public interface Script {
	
	public String getSingleNodeStartStopScript(NODE_ACTION action, int nodeid);
	public String getAllNodeStartStopScript(NODE_ACTION action);
	public String getKillScriptForProcess(long pid);
	public String getProcessIdScript(int node_id);
	public String getForceShutDownScript();

}
