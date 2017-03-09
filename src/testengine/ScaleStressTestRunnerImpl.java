package testengine;

public class ScaleStressTestRunnerImpl extends ScaleStressTestRunner {

	private MonkeyRunner monkeyRunner;
	private int initialNodes=0;
	
	public ScaleStressTestRunnerImpl(int initialNodes) {
		nodeManager= new PRPCNodeManager();
		this.initialNodes = initialNodes;
		monkeyRunner= new MonkeyRunner(nodeManager);
	}
	
	@Override
	public void start() {
		for(int i =1;i<=initialNodes;i++)
		{
			nodeManager.startNode(i);
		}
		
	}
	
	@Override
	public void startAllNodes() {
		nodeManager.startALLNodes();
	}
	
	@Override
	public void terminate()
	{
		if(monkeyRunner!= null)
			monkeyRunner.stop();
		nodeManager.stopALLNodes();
	}
	
	@Override
	public void terminateNode(int node_no) {
		for(int i= 1;i<= node_no;i++) {
			nodeManager.stopNode(i);
		}
	}

	
	@Override
	public void enableStressTest() {
		monkeyRunner.start();
		
	}
	
	
	@Override
	public void addNode(int count) {
		int successCount= 0;
		for(int i= 1;i< nodeManager.MAX_NODES;i++) {
			Node node = nodeManager.clusterNodes.get(i);
			if(node== null || node.getStatus()!= NodeStatus.ACTIVE) {
				nodeManager.startNode(i);
				successCount++;
				if(successCount== count)
					break;
			}
		}
	}


}
