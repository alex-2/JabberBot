package ws.raidrush.alex2.jabberbot.commands;

import java.util.Arrays;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * 
 * Example plugin class for the raid-rush jabber bot
 * 
 * @author Alex²
 *
 */
public class Repeat extends ACommand {

	/**
	 * 
	 * You should not mess with this constructor since its called reflective and sets everything up
	 * 
	 * @param jabberRoom object to manipulate anything regarding the channel
	 *        http://www.igniterealtime.org/builds/smack/docs/latest/javadoc/org/jivesoftware/smackx/muc/MultiUserChat.html
	 * @param message object to get information about the message holding the command (sender, full message ...)
	 *        http://www.igniterealtime.org/builds/smack/docs/latest/javadoc/org/jivesoftware/smack/packet/Message.html
	 * @param parameters pre extracted parameters basically coming from message.getBody() - feel free to do your own stuff
	 */
	public Repeat(MultiUserChat jabberRoom, Message message, String[] parameters) {
		super(jabberRoom, message, parameters);
	}

	/**
	 * Entrypoint for your plugin. This method is called within a thread
	 */
	@Override
	protected void doActions() {

		/**
		 * Meta Information, feel free to add own Keys
		 */
		addMetaInformation("Autor", Base64.decode("QWxleLI=").toString());
		addMetaInformation("Name", getClass().getSimpleName());
		addMetaInformation("Version", "1.0");
		addMetaInformation("Description", "Demo Plugin. Repeats given parameters.");


		String commandFrom = extractNick(getMessage().getFrom()); //Get name of user who sent the Command
		
		String[] parameters = getParameters(); //Get the parameters which the user might have supplied

		try {
			if (parameters == null) { //If the user gave us no parameters
				getJabberRoom().sendMessage(commandFrom + " just asked me to repeat nothing");
			} else { //If the user gave us parameters
				getJabberRoom().sendMessage(commandFrom + " just told me to repeat those parameters: " + Arrays.toString(parameters));
			}

		} catch (XMPPException e) {
			System.err.println("Error while sending message :-/"); //Fuck
		}
	}

	/**
	 * Fullname -> nickname
	 * @param fullName #raidrush@conference.jabber.ccc.de/QWxleLI
	 * @return QWxleLI
	 */
	private String extractNick(String fullName) {
		return fullName.substring(getMessage().getFrom().indexOf("/"));
	}

}
