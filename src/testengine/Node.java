package testengine;

public class Node {

	int nodeid;
	long pid;
	NodeStatus status;
	
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
