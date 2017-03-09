package testengine;

public abstract class ScaleStressTestRunner {
	
	NodeManager nodeManager;

	public abstract void start();
	public abstract void terminate();
	public abstract void addNode(int count);
	public abstract void startAllNodes();

	public abstract void terminateNode(int node_no);
	public abstract void enableStressTest();
}
