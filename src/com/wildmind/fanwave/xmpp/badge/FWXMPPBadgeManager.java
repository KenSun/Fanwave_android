package com.wildmind.fanwave.xmpp.badge;

import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.xmpp.packet.FWBadgeExtension.BadgeExtension;
import com.wildmind.fanwave.xmpp.packet.InfoExtension;
import com.wildmind.fanwave.xmpp.packet.InfoExtension.Info;
import com.wildmind.fanwave.xmpp.provider.BadgeProvider;
import com.wildmind.fanwave.xmpp.provider.InfoProvider;

public class FWXMPPBadgeManager {

	static {
		ProviderManager.getInstance().addExtensionProvider(BadgeExtension.ELEMENT_NAME,
				BadgeExtension.NAMESPACE, new BadgeProvider());
		ProviderManager.getInstance().addExtensionProvider(InfoExtension.ELEMENT_NAME,
				InfoExtension.NAMESPACE, new InfoProvider());
	}
	
	private XMPPConnection connection;
	private MessageTypeFilter badge_message_filter = new MessageTypeFilter(Message.Type.badge);
	private BadgeMessageListener badge_message_listener = new BadgeMessageListener();
	private CopyOnWriteArrayList<FWBadgeListener> badge_listeners = new CopyOnWriteArrayList<FWBadgeListener>();
	
	// constructor
	//
	public FWXMPPBadgeManager (XMPPConnection connection) {
		this.connection = connection;
		connection.addPacketListener(badge_message_listener, badge_message_filter);
	}
	
	/**
	 * Clean up resources of this muc manager.
	 */
	public void clear () {
		connection.removePacketListener(badge_message_listener);
		badge_listeners.clear();
		connection = null;
		badge_message_filter = null;
		badge_message_listener = null;
		badge_listeners = null;
	}
	
	public void addBadgeListener (FWBadgeListener listener) {
		if (!badge_listeners.contains(listener))
			badge_listeners.add(listener);
	}
	
	public void removeBadgeListener (FWBadgeListener listener) {
		badge_listeners.remove(listener);
	}
	
	public void fireBadgeEvent (String badge_id) {
		for (FWBadgeListener listener:badge_listeners)
			listener.receiveBadge(badge_id);
	}
	
	public void fireProgramBadgeEvent (String badge_id, String programTitle) {
		for (FWBadgeListener listener:badge_listeners)
			listener.receiveBadgeFromProgram(badge_id, programTitle);
	}
	
	private void processBadge (Message message) {
		BadgeExtension badge_ext = (BadgeExtension) message.getExtension( BadgeExtension.ELEMENT_NAME,
																		  BadgeExtension.NAMESPACE);
		InfoExtension info_ext = (InfoExtension) message.getExtension( InfoExtension.ELEMENT_NAME,
																	   InfoExtension.NAMESPACE);
		String badge_id = badge_ext.getBadgeId();
		Info badge_info = info_ext.getInfo();
		String programTitle = badge_info.getValue("programTitle");
		String username = badge_info.getValue("username");
		
		// if not user's badge message, just ignore
		if (!username.equals(AccountManager.getCurrentUser().getUsername()))
			return;
		
		if (programTitle != null && programTitle.length() > 0)
			fireProgramBadgeEvent(badge_id, programTitle);
		else
			fireBadgeEvent(badge_id);
	}
	
	private class BadgeMessageListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			processBadge((Message) packet);
		}
	}
	
	public interface FWBadgeListener {

		public void receiveBadge(String badge_id);
		
		public void receiveBadgeFromProgram(String badge_id, String programTitle);
	}
}
