package testengine;

public abstract class TestRunner {
	NodeManager nodeManager;
	//public abstract void start();
	//public abstract void terminate();
	public abstract void startInitialNodes();
	public abstract void runWithAllNodes();
	public abstract void runWithNodes(int node_no);
	public abstract void terminateAllNodes();
	public abstract void TerminateNodes(int node_no);
	public abstract void RestartNode(int node_no);
	public abstract void stressTestEnabled();
}
