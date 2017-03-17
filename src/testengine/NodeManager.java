package testengine;
import static testengine.Properties.PORT;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
/**
 * Represents a cluster group, which might contain more than one nodes, in a host.
 * 
 * @author jenah
 *
 */
public abstract class NodeManager {
	
	private Queue<Session> sessionPool;
	private String user;
	private String password;
	private String host;
	
	Map<Integer, Node> clusterNodes;
	
	public NodeManager(String user, String password, String host) {
		clusterNodes= new HashMap<>();
		sessionPool= new LinkedList<>();
		this.user= user;
		this.password= password;
		this.host= host;
	}
	
	public abstract int getTotalNodes();
	public abstract boolean startALLNodes();
	public abstract boolean startNode(int nodeid);
	
	public abstract boolean stopALLNodes();
	public abstract boolean stopNode(int nodeid);
	
	public final synchronized Session getSession() throws JSchException, IOException {
		if(sessionPool.size()== 0) {
			// create one & return
			sessionPool.add( SSHConnector.getSSHSession(user, password, host, PORT) );
		}
		return sessionPool.remove();
	}
	
	public final synchronized void returnSession(Session session) {
		if(session!= null)
			sessionPool.offer(session);
	}
	
	public int getMaxNode() {
		return Properties.getMaxNode();
	}
	
	public int getCurrentNodeCount() {
		return clusterNodes.size();
	}
}
