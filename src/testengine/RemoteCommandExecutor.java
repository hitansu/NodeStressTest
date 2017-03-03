package testengine;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteCommandExecutor implements CommandExecutor {

	NodeManager nodeMngr;

	public RemoteCommandExecutor(NodeManager nodeMngr) {
		this.nodeMngr = nodeMngr;
	}

	@Override
	public boolean execute(String command) {
		Session session = null;
		try {
			session = nodeMngr.getSession();
			executeCommand(session, command);
			return true;
		} catch (JSchException | IOException e) {
			e.printStackTrace();
		} finally {
			nodeMngr.returnSession(session);
		}
		return false;
	}

	private void executeCommand(Session session, String CMD) throws JSchException, IOException {
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(CMD);
		channel.setInputStream(null);
		((ChannelExec) channel).setErrStream(System.err);
		InputStream in = channel.getInputStream();
		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (in.available() > 0)
					continue;
				System.out.println("exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		channel.disconnect();
		// session.disconnect();
	}

}
