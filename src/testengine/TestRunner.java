package testengine;

public abstract class TestRunner {
	NodeManager nodeManager;
	public abstract void start();
	public abstract void terminate();
	public abstract void runWithAllNodes();
	public abstract void runWithNodes(int node_no);
}
