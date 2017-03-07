package testengine;
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class StressTestRunner extends TestRunner {

	ExecutorService newFixedThreadPool;
	ScheduledExecutorService scheduler;
	int next_server= 1;
	int initialNodes=0;
	
	
	public StressTestRunner(int initialNodes) {
		newFixedThreadPool = Executors.newFixedThreadPool(5);
		nodeManager= new PRPCNodeManager();
		scheduler =  Executors.newScheduledThreadPool(5);
		this.initialNodes = initialNodes;
	}
	
	public void startInitialNodes() {
		for(int i =1;i<=initialNodes;i++)
		{
			nodeManager.startNode(i);
		}
		
	}
	
	@Override
	public void runWithAllNodes() {
		newFixedThreadPool.execute(new StartTask(nodeManager, true));
	}

	@Override
	public void runWithNodes(int node_no) {
		for(int i= 1;i<= node_no;i++) {
			newFixedThreadPool.execute(new StartTask(nodeManager, i));
		}
	}
	
	@Override
	public void terminateAllNodes()
	{
		newFixedThreadPool.execute(new EndTask(nodeManager, true));
	}
	
	@Override
	public void TerminateNodes(int node_no) {
		for(int i= 1;i<= node_no;i++) {
			newFixedThreadPool.execute(new EndTask(nodeManager, i));
		}
	}

	@Override
	public void RestartNode(int node_no)
	{			
		newFixedThreadPool.execute(new RestartTask(nodeManager, node_no));
	}

	@Override
	public void stressTestEnabled() {
		// TODO Auto-generated method stub
		
		newFixedThreadPool.execute(new StartTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
		newFixedThreadPool.execute(new StartTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
	
		newFixedThreadPool.execute(new EndTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
		newFixedThreadPool.execute(new EndTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
		
		newFixedThreadPool.execute(new RestartTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
		newFixedThreadPool.execute(new RestartTask(nodeManager, ThreadLocalRandom.current().nextInt(1,16)));
		
	}
	

	

	static class StartTask implements Runnable {
		
		NodeManager nodeMngr;
		int server_no;
		boolean isALL= false;
		
		public StartTask(NodeManager nodeMngr, int server_no) {
			this.nodeMngr= nodeMngr;
			this.server_no= server_no;
		}
		
		public StartTask(NodeManager nodeMngr, boolean isALL) {
			this.nodeMngr= nodeMngr;
			this.isALL= true;
		}

		@Override
		public void run() {
			if(isALL) {
				nodeMngr.startALLNodes();
			} else {
				nodeMngr.startNode(server_no);
			}
			
		}
	}
	
	
	static class EndTask implements Runnable {
		
		NodeManager nodeMngr;
		int server_no;
		boolean isALL= false;
		
		public EndTask(NodeManager nodeMngr, int server_no) {
			this.nodeMngr= nodeMngr;
			this.server_no= server_no;
		}
		
		public EndTask(NodeManager nodeMngr, boolean isALL) {
			this.nodeMngr= nodeMngr;
			this.isALL= true;
		}

		@Override
		public void run() {
			if(isALL) {
				nodeMngr.stopALLNodes();
			} else {
				nodeMngr.stopNode(server_no);
			}
			
		}
	}


	static class RestartTask implements Runnable {
		
		NodeManager nodeMngr;
		int server_no;
		boolean isALL= false;
		
		public RestartTask(NodeManager nodeMngr, int server_no) {
			this.nodeMngr= nodeMngr;
			this.server_no= server_no;
		}
		
		public RestartTask(NodeManager nodeMngr, boolean isALL) {
			this.nodeMngr= nodeMngr;
			this.isALL= true;
		}

		@Override
		public void run() {
			if(isALL) {
				nodeMngr.stopALLNodes();
				nodeMngr.startALLNodes();
			} else {
				nodeMngr.stopNode(server_no);
				nodeMngr.startNode(server_no);
			}
			
		}
	}


}
