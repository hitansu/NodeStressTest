package testengine;

import java.util.ArrayList;
import java.util.List;

public class PRPCNodeManager extends NodeManager {
	
	private Script script;
	private RemoteCommandExecutor remoteExecutor;
	
	public PRPCNodeManager(String user, String password, String host) {
		super(user, password, host);
		script= new NodeScriptBuilder();
		remoteExecutor= new RemoteCommandExecutor(this, password);
	}

	@Override
	public synchronized int getTotalNodes() {
		return clusterNodes.size();
	}

	@Override
	public boolean startALLNodes() {
		try {
			String COMMAND = script.getAllNodeStartStopScript(NODE_ACTION.START);
			boolean isSucceed = remoteExecutor.execute(COMMAND);
			Node node= null;
			for(int i= 1;i<=MAX_NODES;i++) {
				List<String> pids= new ArrayList<String>();
				remoteExecutor.executeForResult(script.getProcessIdScript(i), pids);
				System.out.println("Process id: "+i+"::"+Long.parseLong(pids.get(0)));
				node= new Node(i, Long.parseLong(pids.get(0)));
				node.setStatus(NodeStatus.ACTIVE);
				addNodeToList(i, node);
			}
			return isSucceed;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean startNode(int nodeid) {
		boolean isSucceed= false;
		try {
			String COMMAND = script.getSingleNodeStartStopScript(NODE_ACTION.START, nodeid);
			isSucceed = remoteExecutor.execute(COMMAND);
			List<String> pids= new ArrayList<>();
			remoteExecutor.executeForResult(script.getProcessIdScript(nodeid), pids);
			Node node= new Node(nodeid, Long.parseLong(pids.get(0)));
			System.out.println("Process id: "+nodeid+"::"+Long.parseLong(pids.get(0)));
			node.setStatus(NodeStatus.ACTIVE);
			addNodeToList(nodeid, node);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isSucceed;
	}

	@Override
	public boolean stopALLNodes() {
		if(clusterNodes.size()== 0) return true;
		try {
			for(int i= 1;i<=clusterNodes.size();i++) {
				stopNode(i);
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean stopNode(int nodeid) {
		Node node = clusterNodes.get(nodeid);
		long pid;
		String command;
		boolean isSucceed= false;
		if(node== null) {
			List<String> pids= new ArrayList<>();
			remoteExecutor.executeForResult(script.getProcessIdScript(nodeid), pids);
			if(pids.size()>0) {
				pid= Long.parseLong(pids.get(0));
				command= script.getKillScriptForProcess(Long.parseLong(pids.get(0)));
				isSucceed= remoteExecutor.execute(command);
			}
		} else {
			pid= node.getPid();
			command= script.getKillScriptForProcess(pid);
			isSucceed= remoteExecutor.execute(command);
			node.setStatus(NodeStatus.IDLE);
			removeNodeFrmList(node);
		}
		
		return isSucceed;
	}
	
	private synchronized void addNodeToList(int nodeid, Node node) {
		clusterNodes.put(nodeid, node);
	}
	
	private synchronized void removeNodeFrmList(Node node) {
		clusterNodes.remove(node);
	}

}
