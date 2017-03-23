package testengine;

import static testengine.Properties.SERVER_BOOT_TIME;
import static testengine.Properties.WAIT_FOR_SERVER_UP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Scale,Stress test implementation...
 * @author jenah
 *
 */
public class ScaleStressTestRunnerImpl extends ScaleStressTestRunner {

	private MonkeyRunner monkeyRunner;
	private int initialNodes=1;
	private boolean isWaitForServerUp;
	private long serverWaitPeriod;
	private Random rand;
	private boolean aIsStressTestEnabled= false;
	private int THRESHOLD_NODE_COUNT_FOR_STRESS;
	private int totalActiveNodes;
	private int MAX_POSSIBLE_NODE;
	
	public ScaleStressTestRunnerImpl(int initialNodes) {
		rand= new Random();
		nodeManagerContainer= new ArrayList<>();
		addNodeManager(nodeManagerContainer);
		this.initialNodes = initialNodes;
		monkeyRunner= new MonkeyRunner(nodeManagerContainer);
		serverWaitPeriod= SERVER_BOOT_TIME;
		isWaitForServerUp= WAIT_FOR_SERVER_UP;
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
		totalActiveNodes= initialNodes;
		if(aIsStressTestEnabled && totalActiveNodes>= THRESHOLD_NODE_COUNT_FOR_STRESS) {
			startMonkeyRunner();
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
		totalActiveNodes= MAX_POSSIBLE_NODE;
		startMonkeyRunner();
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
		totalActiveNodes= (totalActiveNodes+count) > MAX_POSSIBLE_NODE ? MAX_POSSIBLE_NODE: (totalActiveNodes+count);
		if(aIsStressTestEnabled && totalActiveNodes>= THRESHOLD_NODE_COUNT_FOR_STRESS) {
			startMonkeyRunner();
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
		totalActiveNodes= 0;
	}
	
	@Override
	public void terminateNode(int node_id) {
		getNodeManager().stopNode(node_id);
		totalActiveNodes= (totalActiveNodes-1) < 0 ? 0: (totalActiveNodes-1);
	}

	
	@Override
	public void enableStressTest() {
		aIsStressTestEnabled= true;
		THRESHOLD_NODE_COUNT_FOR_STRESS= (3/4)*MAX_POSSIBLE_NODE;
	}
	
	@Override
	public void enableStressTest(int thresholdNodeCount) {
		aIsStressTestEnabled= true;
		THRESHOLD_NODE_COUNT_FOR_STRESS= thresholdNodeCount;
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
			MAX_POSSIBLE_NODE+= nodeMngr.getMaxNode();
		}
		
	}
	
	private void startMonkeyRunner() {
		if(monkeyRunner!= null && !monkeyRunner.isRunning()) {
			monkeyRunner.start();
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
		while(nodeManagerContainer.get(randomIndex).getTotalNodes()== Properties.getMaxNode() && mSize>0) {
			randomIndex= rand.nextInt(size);
			mSize--;
		}
		
		return nodeManagerContainer.get(randomIndex);	
	}
}
