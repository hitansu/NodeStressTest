package testengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of NodeManager...
 * @author jenah
 *
 */
public class PRPCNodeManager extends NodeManager {
	
	private Script script;
	private RemoteCommandExecutor remoteExecutor;
	
	public PRPCNodeManager(String user, String password, String host) {
		super(user, password, host);
		script= new NodeScriptBuilderUnix();
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
			int MAX_NODES= getMaxNode();
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
			List<String> pids= new ArrayList<>();
			if(checkIfAlreadyExist(nodeid, pids)) {
				Node node= new Node(nodeid, Long.parseLong(pids.get(0)));
				node.setStatus(NodeStatus.ACTIVE);
				addNodeToList(nodeid, node);
				return true;
			}
			String COMMAND = script.getSingleNodeStartStopScript(NODE_ACTION.START, nodeid);
			isSucceed = remoteExecutor.execute(COMMAND);
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
		if(node== null || node.getPid()== Long.MIN_VALUE) {
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
	
	private boolean checkIfAlreadyExist(int nodeid, List<String> pids) {
		remoteExecutor.executeForResult(script.getProcessIdScript(nodeid), pids);
		if(pids.size()== 1) {
			return true;
		}
		return false;
	}
	
	private synchronized void addNodeToList(int nodeid, Node node) {
		clusterNodes.put(nodeid, node);
	}
	
	private synchronized void removeNodeFrmList(Node node) {
		clusterNodes.remove(node);
	}

}
