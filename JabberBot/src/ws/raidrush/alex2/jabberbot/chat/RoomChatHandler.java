package ws.raidrush.alex2.jabberbot.chat;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import ws.raidrush.alex2.jabberbot.commands.handler.CommandHandler;
/**
 * 
 * @author Alex²
 *
 * PacketListener for handling messages coming from channel
 *
 */
public class RoomChatHandler implements PacketListener {

	private final MultiUserChat xmppRoom;

	public RoomChatHandler(MultiUserChat xmppRoom) {
		super();
		this.xmppRoom = xmppRoom;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) { //If the packet is a message-packet
			Message message = (Message) packet;
			if (message.getBody().startsWith(CommandHandler.COMMAND_PREFIX)) //If the message could be a command
				CommandHandler.parseCommand(message, xmppRoom); //Let the CommandHandler do work
		}
	}

}
