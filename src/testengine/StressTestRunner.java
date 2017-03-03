package testengine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StressTestRunner extends TestRunner {

	ExecutorService newFixedThreadPool;
	int next_server= 1;
	
	
	public StressTestRunner() {
		newFixedThreadPool = Executors.newFixedThreadPool(5);
		nodeManager= new PRPCNodeManager();
	}
	
	@Override
	public void start() {
		try {
			nodeManager.startALLNodes();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void terminate() {
		newFixedThreadPool.shutdown();
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
	

}
