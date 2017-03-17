package testengine;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH session connector
 * @author jenah
 *
 */
public class SSHConnector {

	public static Session getSSHSession(String username, String password, String host, int port) throws JSchException, IOException {

		final JSch jsch = new JSch();
		Session session = jsch.getSession(username, host, 22);
		session.setPassword(password);

		final Properties config = new Properties();
		/* bypass host checking */
		config.put("StrictHostKeyChecking", "no");
		session.setTimeout(150000);
		session.setConfig(config);
		session.setDaemonThread(true);

		session.connect();
		if (!session.isConnected()) {
			throw new IOException("Session not connected");
		} else {
			System.out.println("session connected");
		}
		return session;
	}

}
