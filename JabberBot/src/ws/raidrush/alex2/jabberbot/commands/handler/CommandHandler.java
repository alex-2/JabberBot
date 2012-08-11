package ws.raidrush.alex2.jabberbot.commands.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import ws.raidrush.alex2.jabberbot.commands.ACommand;

/**
 * Class for handling command, might consider a rewrite to make it less static. Feel free todo 
 * 
 * @author Alex
 *
 */
public class CommandHandler {

	public static final String COMMAND_PREFIX = "!";
	public static final String PARAMETER_DELIMITER = " ";
	public static final String pkg = "ws.raidrush.alex2.jabberbot.commands";

	public static void parseCommand(Message message, MultiUserChat xmppRoom) {

		// FIXME: needs to become dynamic, too lazy atm
		if (message.getFrom().equals("#raidrush@conference.jabber.ccc.de/Quote")) //The bot is not allowed to give itself commands - endless loop
			return;

		if (message.getBody().length() < 2) //The command must be at least one character long
			return;

		String commandLine = message.getBody();

		String commandName = extractCommandName(commandLine); //Get the command name
		String parameters[] = extractParameters(commandLine); //Get the parameters

		ACommand commandPluginInstance = getPluginInstance(commandName, message, parameters, xmppRoom); //Try to get an instance of the plugin class

		if (commandPluginInstance != null) { 
			commandPluginInstance.executeCommand();
		} else {
			Logger.getRootLogger().error("Problem while loading plugin. (" + commandName + ")");
		}

	}

	/**
	 * Dynamically loads and constructs the plugin class 
	 * 
	 * @param commandName 
	 * @param message
	 * @param parameters
	 * @param xmppRoom
	 * @return an instance of the plugin class
	 */
	private static ACommand getPluginInstance(String commandName, Message message, String[] parameters, MultiUserChat xmppRoom) {
		String className = pkg + "." + commandName;
		try {
			Class<?> commandClass = ClassLoader.getSystemClassLoader().loadClass(className); //Get classloader and load class
			ACommand commandPluginClass = (ACommand) commandClass.getConstructors()[0].newInstance(xmppRoom, message, parameters); //Reflect contructor and create instance
			return commandPluginClass;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Extracts the parameters ouf of the commandline
	 * @param commandLine
	 * @return an array with the parameters the user provided. returns null if none were given
	 */
	private static String[] extractParameters(String commandLine) {
		commandLine = commandLine.trim();

		String[] parameters = null;

		if (commandLine.contains(PARAMETER_DELIMITER)) {
			parameters = commandLine.substring(commandLine.indexOf(PARAMETER_DELIMITER) + 1).split(PARAMETER_DELIMITER);
		}

		return parameters;
	}
	
	/**
	 * Extracts the command name out of the command line and puts it in correct capitalization
	 * @param commandLine
	 * @return the command name which should be equal to the plugin class name -> "Command"
	 */
	private static String extractCommandName(String commandLine) {
		commandLine = commandLine.trim();
		if (commandLine.contains(PARAMETER_DELIMITER)) {
			commandLine = commandLine.substring(1, commandLine.indexOf(PARAMETER_DELIMITER));
		} else {
			commandLine = commandLine.substring(1);
		}
		return commandLine.substring(0, 1).toUpperCase() + commandLine.substring(1).toLowerCase();
	}
}
