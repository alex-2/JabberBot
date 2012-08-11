package ws.raidrush.alex2.jabberbot;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import ws.raidrush.alex2.jabberbot.chat.RoomChatHandler;

/**
 * Class for wrapping the basic XMPP/Jabber functions
 * Pretty self explaining, will comment later due to lazyness
 * @author Alex
 *
 */
public class BasicXMPPBot {

	private final XMPPConnection xmppConnection;

	private final String xmppUser;
	private final String xmppPass;

	private final String room;
	private String nick;

	private MultiUserChat xmppRoom;

	private RoomChatHandler roomChatHandler;

	public BasicXMPPBot(String xmppHost, int xmppPort, String xmppUser, String xmppPass, String nick, String room) {
		super();

		ConnectionConfiguration config = new ConnectionConfiguration(xmppHost, xmppPort);
		xmppConnection = new XMPPConnection(config);

		this.room = room;

		this.xmppUser = xmppUser;
		this.xmppPass = xmppPass;

		this.nick = nick;

	}

	public void connect() {
		if (!xmppConnection.isConnected()) {
			try {
				xmppConnection.connect();
				Logger.getRootLogger().info("Connect successful! (" + xmppConnection.getHost() + ":" + xmppConnection.getPort() + ")");
			} catch (XMPPException e) {
				Logger.getRootLogger().fatal(e.getMessage(), e);
			}
		} else {
			Logger.getRootLogger().info("Already connected. Won't do anything.");
		}

	}

	public void login() {
		if (!xmppConnection.isAuthenticated()) {
			try {
				xmppConnection.login(xmppUser, xmppPass);
				Logger.getRootLogger().info("Login successful! (" + xmppUser + ":***)");
			} catch (XMPPException e) {
				Logger.getRootLogger().error(e.getMessage(), e);
			}
		} else {
			Logger.getRootLogger().info("Already logged in. Won't do anything.");
		}
	}

	public void joinRoom() {
		if (xmppRoom == null) {
			xmppRoom = new MultiUserChat(xmppConnection, room);
			roomChatHandler = new RoomChatHandler(xmppRoom);
			xmppRoom.addMessageListener(roomChatHandler);
		}

		if (!xmppRoom.isJoined()) {
			try {
				DiscussionHistory discussionHistory = new DiscussionHistory();
				discussionHistory.setMaxChars(0);
				xmppRoom.join(nick, "", discussionHistory, 20);
				xmppRoom.changeNickname(nick);
				Logger.getRootLogger().info("Room join successful! (" + xmppRoom.getRoom() + ")");
			} catch (XMPPException e) {
				Logger.getRootLogger().error(e.getMessage(), e);
			}
		} else {
			Logger.getRootLogger().info("Already joined room \"" + xmppRoom.getRoom() + "\" . Won't do anything.");
		}

	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
