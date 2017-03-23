package testengine;

import java.util.List;
import java.util.Random;

/**
 * Randomly picks any node to shut-down & start & restart..
 * @author jenah
 *
 */
public class MonkeyRunner {

	private Thread t;
	private MonkeyTaskRunner r;

	public MonkeyRunner(List<NodeManager> nodeMngrs) {
		r = new MonkeyTaskRunner(nodeMngrs);
		t = new Thread(r, "Monkey Runner");
	}

	public void start() {
		t.start();
		r.enable();
	}

	public void stop() {
		r.disable();
	}
	
	public boolean isRunning() {
		return r.isEnabled== true;
	}

	static class MonkeyTaskRunner implements Runnable {

		private static final String[] types = { "STOP", "RESTART" };
		private boolean isEnabled= false;

		private List<NodeManager> mngrs;
		private Random rand;

		public MonkeyTaskRunner(List<NodeManager> mngrs) {
			this.mngrs = mngrs;
			rand = new Random();
		}
		
		public void disable() {
			isEnabled= false;
		}
		
		public void enable() {
			isEnabled= true;
		}

		@Override
		public void run() {
			long next_sleepTime= 12000;
			while (isEnabled) {
				next_sleepTime= next_sleepTime+rand.nextInt(20000);
				try {
					Thread.sleep(next_sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				NodeManager mngr= getNodeManager(mngrs);
				if(mngr.getTotalNodes()<1)
					continue;
				int serverNo= getNextServer(mngr);
				String TYPE= types[rand.nextInt(types.length)];
				System.out.println("Monkey runner: "+serverNo + " action "+TYPE);
				switch (TYPE) {
					case "STOP":
						mngr.stopNode(serverNo);
						break;
					case "RESTART":
						mngr.stopNode(serverNo);
						mngr.startNode(serverNo);
						break;
				}
				
			}

		}
		
		private NodeManager getNodeManager(List<NodeManager> mngrs) {
			int size = mngrs.size();
			int randomIndex= rand.nextInt(size);
			int mSize= size;
			while(mngrs.get(randomIndex).getTotalNodes()== 7 && mSize>0) {
				randomIndex= rand.nextInt(size);
				mSize--;
			}
			return mngrs.get(randomIndex);
			
		}

		private int getNextServer(NodeManager mngr2) {
			int server_no= rand.nextInt(mngr2.getTotalNodes())+1;
			while(mngr2.clusterNodes.get(server_no)!= null && mngr2.clusterNodes.get(server_no).getStatus()!= NodeStatus.ACTIVE) {
				server_no= rand.nextInt(mngr2.getTotalNodes())+1;
			}
			return server_no;
		}
	}

}
