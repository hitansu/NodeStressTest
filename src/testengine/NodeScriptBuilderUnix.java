package testengine;

import static testengine.Properties.ALL_SERVER_SCRIPT;
import static testengine.Properties.ROOT_DIR;
import static testengine.Properties.getServerName;

public class NodeScriptBuilderUnix implements Script {

	@Override
	public String getSingleNodeStartStopScript(NODE_ACTION action, int nodeid) {
		String script= "";
		if(action.equals(NODE_ACTION.START))
			script= "sh"+" "+ROOT_DIR+getServerName(nodeid-1)+"/"+"bin/"+"startup.sh";
		else
			script= "sh"+" "+ROOT_DIR+getServerName(nodeid-1)+"/"+"bin/"+"shutdown.sh";
		
		if(script== null || script.isEmpty()) 
			throw new IllegalArgumentException("Script cant be empty");
		
		return script;
	}

	@Override
	public String getAllNodeStartStopScript(NODE_ACTION action) {
		String script= "";
		if(action.equals(NODE_ACTION.START))
			script= "sh"+" "+ROOT_DIR+ALL_SERVER_SCRIPT+" "+"start";
		else
			script= "sh"+" "+ROOT_DIR+ALL_SERVER_SCRIPT+" "+"stop";
		
		if(script== null || script.isEmpty()) 
			throw new IllegalArgumentException("Script cant be empty");
		
		return script;
	}

	@Override
	public String getKillScriptForProcess(long pid) {
		return "kill -9 "+pid;
	}

	@Override
	public String getProcessIdScript(int node_id) {
		return "ps -eaf | grep tomcat | "+"grep "+Properties.tomcatServersLoc[node_id-1] +"| grep -v grep | awk '{print $2}'";
	}

	@Override
	public String getForceShutDownScript() {
		return "ps -eaf | grep tomcat | grep -v grep | awk '{print $2}' | xargs kill -9";
	}

}
