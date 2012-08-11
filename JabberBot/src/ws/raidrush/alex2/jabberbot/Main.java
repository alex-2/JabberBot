package ws.raidrush.alex2.jabberbot;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

	/**
	 * Still in debugging state
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.getRootLogger().setLevel(Level.DEBUG);

		BasicXMPPBot quote = new BasicXMPPBot("jabber.ccc.de", 5222, "user", "pass", "Nick", "channel@conference.jabber.ccc.de");

		quote.connect();
		quote.login();
		quote.joinRoom();
		
		while (true) { //lol

		}
	}
}
