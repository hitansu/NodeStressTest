package testengine;

public class PRPCNodeManager extends NodeManager {
	
	ScriptBuilder scriptBuilder= new ScriptBuilder();
	RemoteCommandExecutor remoteExecutor;
	
	public PRPCNodeManager() {
		remoteExecutor= new RemoteCommandExecutor(this);
	}

	@Override
	public int getTotalNodes() {
		return clusterNodes.size();
	}

	@Override
	public boolean startALLNodes() {
		try {
			// start node
			String COMMAND = scriptBuilder.getALLNodeScript(NODE_ACTION.START);
			boolean isSucceed = remoteExecutor.execute(COMMAND);
			Node node= null;
			for(int i= 1;i<=MAX_NODES;i++) {
				node= new Node(i);
				node.setStatus(NodeStatus.ACTIVE);
				addNodeToList(i, node);
			}
			return true;
		} catch(Exception e) {
			//
		}
		return false;
	}

	@Override
	public boolean startNode(int nodeid) {
		Node node= new Node(nodeid);
		try {
			// start node
			String COMMAND = scriptBuilder.getSingleNodeScript(NODE_ACTION.START, nodeid);
			boolean isSucceed = remoteExecutor.execute(COMMAND);
			node.setStatus(NodeStatus.ACTIVE);
			addNodeToList(nodeid, node);
		} catch(Exception e) {
			node.setStatus(NodeStatus.FAILED_START);
		}
		return false;
	}

	@Override
	public boolean stopALLNodes() {
		String command = scriptBuilder.getALLNodeScript(NODE_ACTION.STOP);
		try {
			boolean isSucceed = remoteExecutor.execute(command);
			Node node= null;
			for(int i= 1;i<=MAX_NODES;i++) {
				node= new Node(i);
				node.setStatus(NodeStatus.IDLE);
				removeNodeFrmList(node);
			}
			return true;
		} catch(Exception e) {
			
		}
		
		return false;
	}

	@Override
	public boolean stopNode(int nodeid) {
		Node node = clusterNodes.get(nodeid);
		if(node.getStatus()== NodeStatus.ACTIVE) {
			// stop node
			String command = scriptBuilder.getSingleNodeScript(NODE_ACTION.START, nodeid);
			boolean isSucceed = remoteExecutor.execute(command);
			node.setStatus(NodeStatus.IDLE);
			removeNodeFrmList(node);
			return true;
		}
		return false;
	}
	
	private void addNodeToList(int nodeid, Node node) {
		clusterNodes.put(nodeid, node);
	}
	
	private void removeNodeFrmList(Node node) {
		clusterNodes.remove(node);
	}

}
