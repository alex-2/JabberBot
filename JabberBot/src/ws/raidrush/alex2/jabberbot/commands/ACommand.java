package ws.raidrush.alex2.jabberbot.commands;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Abstract class which has to be implemented by all plugin/command classes
 * {@see Repeat} for example
 * @author Alex
 *
 */
public abstract class ACommand {

	protected final Map<String, String> metaInformation = new HashMap<String, String>();

	private final MultiUserChat jabberRoom;
	private final Message message;
	private final String[] parameters;

	public ACommand(MultiUserChat jabberRoom, Message message, String[] parameters) {
		super();
		this.jabberRoom = jabberRoom;
		this.message = message;
		this.parameters = parameters;
	}

	/**
	 * Add meta information
	 * @param key the type of meta information
	 * @param value the content
	 */
	protected void addMetaInformation(String key, String value) {
		metaInformation.put(key, value);
	}

	/**
	 * Entry point for the plugin stuff
	 */
	protected abstract void doActions();

	/**
	 * Method which will be executed by the CommandHandler
	 */
	public void executeCommand() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				doActions();
			}
		});
		thread.start();
	}

	public Map<String, String> getMetaInformation() {
		return metaInformation;
	}

	public MultiUserChat getJabberRoom() {
		return jabberRoom;
	}

	public Message getMessage() {
		return message;
	}

	public String[] getParameters() {
		return parameters;
	}

}
