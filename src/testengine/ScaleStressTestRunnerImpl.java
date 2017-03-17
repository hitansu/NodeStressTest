package testengine;

import static testengine.Properties.SERVER_BOOT_TIME;
import static testengine.Properties.WAIT_FOR_SERVER_UP;
import static testengine.Properties.getMaxNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScaleStressTestRunnerImpl extends ScaleStressTestRunner {

	private MonkeyRunner monkeyRunner;
	private int initialNodes=1;
	private boolean isWaitForServerUp;
	private long serverWaitPeriod;
	private Random rand;
	
	public ScaleStressTestRunnerImpl(int initialNodes) {
		rand= new Random();
		nodeManagerContainer= new ArrayList<>();
		addNodeManager(nodeManagerContainer);
		this.initialNodes = initialNodes;
		monkeyRunner= new MonkeyRunner(nodeManagerContainer);
		serverWaitPeriod= SERVER_BOOT_TIME;
		isWaitForServerUp= WAIT_FOR_SERVER_UP;
	}
	
	private void addNodeManager(List<NodeManager> nodeManagerContainer) {
		String[] hosts= Properties.HOST.split(",");
		String[] users= Properties.USER.split(",");
		String[] passwords= Properties.PASSWORD.split(",");
		if(hosts.length!= users.length || users.length!= passwords.length) {
			throw new IllegalStateException("For each host username & password should be present");
		}
		
		for(int i= 0;i<hosts.length;i++) {
			NodeManager nodeMngr= new PRPCNodeManager(users[i], passwords[i], hosts[i]);
			nodeManagerContainer.add(nodeMngr);
		}
		
	}

	public ScaleStressTestRunnerImpl() {
		this(1);
	}
	
	@Override
	public void start() {
		for(int i =1;i<=initialNodes;i++)
		{
			getNodeManager().startNode(i);
		}
		if(isWaitForServerUp) {
			waitForServerUp(serverWaitPeriod);
		}
	}
	
	@Override
	public void startAllNodes() {
		for(NodeManager nodeManager: nodeManagerContainer) {
			nodeManager.startALLNodes();
		}
		if(isWaitForServerUp) {
			waitForServerUp(serverWaitPeriod);
		}
	}
	
	@Override
	public void terminate()
	{
		if(monkeyRunner!= null)
			monkeyRunner.stop();
		for(NodeManager nodeManager: nodeManagerContainer) {
			nodeManager.stopALLNodes();
		}
	}
	
	@Override
	public void terminateNode(int node_no) {
		for(int i= 1;i<= node_no;i++) {
			getNodeManager().stopNode(i);
		}
	}

	
	@Override
	public void enableStressTest() {
		monkeyRunner.start();
	}
	
	
	@Override
	public void addNode(int count) {
		int successCount= 0;
		int MAX= nodeManagerContainer.get(0).getMaxNode();
		for(int i= 1;i< MAX;i++) {
			NodeManager nodeManager= getNodeManager();
			Node node = nodeManager.clusterNodes.get(i);
			if(node== null || node.getStatus()!= NodeStatus.ACTIVE) {
				nodeManager.startNode(i);
				successCount++;
				if(successCount== count)
					break;
			}
		}
		if(isWaitForServerUp) {
			waitForServerUp(serverWaitPeriod);
		}
	}
	
	private void waitForServerUp(long wait_time) {
		try {
			Thread.sleep(wait_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private NodeManager getNodeManager() {
		int size = nodeManagerContainer.size();
		int randomIndex= rand.nextInt(size);
		int mSize= size;
		while(nodeManagerContainer.get(randomIndex).getTotalNodes()== getMaxNode() && mSize>0) {
			randomIndex= rand.nextInt(size);
			mSize--;
		}
		
		return nodeManagerContainer.get(randomIndex);
		
	}
}
