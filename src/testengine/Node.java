package testengine;

/**
 * Represents each Node.
 * @author jenah
 *
 */
public class Node {

	private int nodeid;
	private long pid= Long.MIN_VALUE;
	private NodeStatus status;
	
	public Node(int nodeid, long pid) {
		this.nodeid= nodeid;
		this.pid= pid;
	}
	
	public long getPid() {
		return pid;
	}
	
	public void setStatus(NodeStatus status) {
		this.status= status;
	}
	
	public NodeStatus getStatus() {
		return status;
	}
	
	public int getNodeID() {
		return nodeid;
	}
	
    @Override
    public int hashCode() {
    	Integer i= nodeid;
    	return i.hashCode();
    }
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Node)) return false;
        Node that= (Node) obj;
        if(that.nodeid== this.nodeid) {
        	return true;
        }
		return false;
	}
}
