package testengine;

public class ScriptBuilder {

	public String getSingleNodeScript(NODE_ACTION action, int nodeid) {
		String script= "";
		if(action.equals(NODE_ACTION.START)) {
			script= "sh"+" "+Properties.ROOT_DIR+Properties.SINGLE_SERVER_SCRIPT+" "+"start";
		}else {
			script= "sh"+" "+Properties.ROOT_DIR+Properties.SINGLE_SERVER_SCRIPT+" "+"stop";
		}
		if(script== null || script.isEmpty()) throw new IllegalArgumentException("Script cant be empty");
		return script;
	}
	
	public String getALLNodeScript(NODE_ACTION action) {
		String script= "";
		if(action.equals(NODE_ACTION.START)) {
			script= "sh"+" "+Properties.ROOT_DIR+Properties.ALL_SERVER_SCRIPT+" "+"start";
		}else {
			script= "sh"+" "+Properties.ROOT_DIR+Properties.ALL_SERVER_SCRIPT+" "+"stop";
		}
		if(script== null || script.isEmpty()) throw new IllegalArgumentException("Script cant be empty");
		return script;
	}
}
