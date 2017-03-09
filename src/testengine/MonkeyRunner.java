package testengine;

import java.util.Random;

public class MonkeyRunner {

	private Thread t;
	private MonkeyTaskRunner r;

	public MonkeyRunner(NodeManager nodeMngr) {
		r = new MonkeyTaskRunner(nodeMngr);
		t = new Thread(r, "Monkey Runner");
	}

	public void start() {
		t.start();
		r.enable();
	}

	public void stop() {
		r.disable();
	}

	static class MonkeyTaskRunner implements Runnable {

		private static final String[] types = { "START", "STOP", "RESTART" };
		private boolean isEnabled= false;

		private NodeManager mngr;
		private Random rand;

		public MonkeyTaskRunner(NodeManager mngr) {
			this.mngr = mngr;
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
				next_sleepTime= rand.nextInt(80000-12000);
				try {
					Thread.sleep(next_sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(mngr.getTotalNodes()<1)
					continue;
				int serverNo= getNextServer(mngr);
				String TYPE= types[rand.nextInt(types.length)];
				System.out.println("Monkey runner: "+serverNo + " action "+TYPE);
				switch (TYPE) {
					case "START":
						mngr.startNode(serverNo);
						break;
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

		private int getNextServer(NodeManager mngr2) {
			int server_no= rand.nextInt(mngr.getTotalNodes())+1;
			while(mngr.clusterNodes.get(server_no)!= null && mngr.clusterNodes.get(server_no).status!= NodeStatus.ACTIVE) {
				server_no= rand.nextInt(mngr.getTotalNodes())+1;
			}
			return server_no;
		}
	}

}
