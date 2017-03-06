package testengine;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public abstract class NodeManager {
	
	public int MAX_NODES= 7;
	
	Map<Integer, Node> clusterNodes;
	Queue<Session> sessionPool;
	
	public NodeManager() {
		clusterNodes= new HashMap<>();
		sessionPool= new LinkedList<>();
	}
	
	public abstract int getTotalNodes();
	public abstract boolean startALLNodes();
	public abstract boolean startNode(int nodeid);
	
	public abstract boolean stopALLNodes();
	public abstract boolean stopNode(int nodeid);
	
	public final synchronized Session getSession() throws JSchException, IOException {
		if(sessionPool.size()== 0) {
			// create one & return
			sessionPool.add( SSHConnector.getSSHSession(Properties.USER, Properties.PASSWORD, Properties.HOST, Properties.PORT) );
		}
		return sessionPool.remove();
	}
	
	public final synchronized void returnSession(Session session) {
		if(session!= null)
			sessionPool.offer(session);
	}

}
