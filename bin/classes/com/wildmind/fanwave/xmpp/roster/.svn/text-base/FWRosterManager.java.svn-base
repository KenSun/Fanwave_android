package com.wildmind.fanwave.xmpp.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.util.StringUtils;

import com.wildmind.fanwave.user.TVUser;

public class FWRosterManager {

	private XMPPConnection connection;
	private ConcurrentHashMap<String, TVUser> user_list = new ConcurrentHashMap<String, TVUser>();
	private ConcurrentHashMap<String, TVUser> accepting_list = new ConcurrentHashMap<String, TVUser>();
	private FWRosterListener roster_listener = new FWRosterListener();
	private SubscriptionListener subscription_listener = new SubscriptionListener();
	private CopyOnWriteArrayList<FWFriendListener> friend_listeners = new CopyOnWriteArrayList<FWFriendListener>();
	
	// constructor
	//
	public FWRosterManager (XMPPConnection connection) {
		this.connection = connection;
		this.connection.addPacketListener(subscription_listener, new PacketTypeFilter(Presence.class));
	}
	
	// variable getters
	// 
	public FWRosterListener getRosterListener () {
		return roster_listener;
	}
	
	/**
	 * Clean up resources of this roster manager.
	 */
	public void clear() {
		connection.removePacketListener(subscription_listener);
		user_list.clear();
		accepting_list.clear();
		friend_listeners.clear();
		connection = null;
		user_list = null;
		accepting_list = null;
		roster_listener = null;
		subscription_listener = null;
		friend_listeners = null;
	}
	
	public void addFriendListener (FWFriendListener listener) {
		if (!friend_listeners.contains(listener))
			friend_listeners.add(listener);
	}
	
	/**
	 * Remove a friend listener.
	 * @param listener
	 */
	public void removeFriendListener (FWFriendListener listener) {
		friend_listeners.remove(listener);
	}
	
	/**
	 * Subscribe an user.
	 * @param jid
	 * @return
	 */
	public boolean subscribeUser (String jid) {
		RosterEntry entry = connection.getRoster().getEntry(jid);
		if (entry != null &&
			(entry.getType() == RosterPacket.ItemType.both ||
			 entry.getType() == RosterPacket.ItemType.to) )
			return false;
		
		Presence presence = new Presence(Presence.Type.subscribe);
		presence.setTo(jid);
		connection.sendPacket(presence);
		
		return true;
	}
	
	/**
	 * Subscribed an user.
	 * @param jid
	 * @return
	 */
	public boolean subscribedUser (String jid) {
		RosterEntry entry = connection.getRoster().getEntry(jid);
		if (entry != null &&
			(entry.getType() == RosterPacket.ItemType.both ||
			 entry.getType() == RosterPacket.ItemType.from))
			return false;
		
		Presence presence = new Presence(Presence.Type.subscribed);
		presence.setTo(jid);
		connection.sendPacket(presence);
		
		return true;
	}
	
	public void unsubscribeUser (String jid) {
		Presence presence = new Presence(Presence.Type.unsubscribe);
		presence.setTo(jid);
		connection.sendPacket(presence);
	}
	
	public void unsubscribedUser (String jid) {
		Presence presence = new Presence(Presence.Type.unsubscribed);
		presence.setTo(jid);
		connection.sendPacket(presence);
	}
	
	/**
	 * Accept an user. 
	 * We have to check if the user is already subscribed or not.
	 * If is subscribed, just send subscribe.
	 * Else, send subscribed first. Then wait the iq set from packet due to this method and auto send a subscribe to the user.
	 * The auto send action is implemented in entriesAdded method of FWRosterListener nested class.
	 * @param user
	 * @return
	 */
	public boolean acceptUser (TVUser user) {
		boolean success = false;
		RosterEntry entry = connection.getRoster().getEntry(user.getJid());
		if (isPendingEntry(entry)) 
			success = subscribeUser(user.getJid());
		else {
			if (success = subscribedUser(user.getJid()))
				accepting_list.put(user.getJid(), user);
		}
		
		// We assume the user is a friend since we accept his request.
		if (success) {
			ArrayList<TVUser> users = new ArrayList<TVUser>();
			users.add(user);
			fireFriendEvent(users);
		}
		
		return success;
	}
	
	/**
	 * Reject an user.
	 * @param jid
	 */
	public void rejectUser (String jid) {
		RosterEntry entry = connection.getRoster().getEntry(jid);
		if (entry == null) {
			RosterPacket packet = new RosterPacket();
	        packet.setType(IQ.Type.SET);
	        
	        RosterPacket.Item item = new RosterPacket.Item(jid, "");
	        item.setItemType(RosterPacket.ItemType.remove);
	        packet.addRosterItem(item);
	        connection.sendPacket(packet);
		}
		else
			removeUser(jid);
	}
	
	/**
	 * Remove an user.
	 * @param jid
	 */
	public void removeUser (String jid) {
		RosterEntry entry = connection.getRoster().getEntry(jid);
		if (entry == null)
			return;
		
		try {
			connection.getRoster().removeEntry(entry);
			user_list.remove(jid);
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get roster entries collection.
	 * @return Collection<RosterEntry>
	 */
	public Collection<RosterEntry> getRoster () {
		return connection.getRoster().getEntries();
	}
	
	/**
	 * Process 'subscribe' presence.
	 * @param presence
	 */
	public void processSubscribe (Presence presence) {
		RosterEntry entry = connection.getRoster().getEntry(presence.getFrom());
		
		TVUser user = new TVUser();
		user.setUsername(presence.getUsername());
		user.setNickname(presence.getNickname());
		user.setBadgeId(presence.getBadgeId());
		user.setJid(StringUtils.parseBareAddress(presence.getFrom()));
		user_list.put(user.getJid(), user);
		
		if (entry == null || entry.getType() == RosterPacket.ItemType.none) {
			// receive request
			fireRequestEvent(user);
			
		} else if (entry != null && entry.getType() == RosterPacket.ItemType.to) {
			// receive accept
			fireAcceptEvent(user);
			// auto send subscribed
			subscribedUser(user.getJid());
		}
	}
	
	/**
	 * Process 'subscribed' presence.
	 * @param presence
	 */
	public void processSubscribed (Presence presence) {
		// do nothing
	}
	
	/**
	 * Deliver friend event to all friend listeners.
	 * @param user
	 */
	public void fireFriendEvent (ArrayList<TVUser> users) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveFriends(users);
	}
	
	/**
	 * Deliver request event to all friend listeners.
	 * @param user
	 */
	public void fireRequestEvent (TVUser user) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveRequest(user);
	}
	
	/**
	 * Deliver accept event to all friend listeners.
	 * @param user
	 */
	public void fireAcceptEvent (TVUser user) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveAccept(user);
	}
	
	/**
	 * Deliver remove event to all friend listeners.
	 * @param user
	 */
	public void fireRemoveEvent (ArrayList<TVUser> users) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveRemoves(users);
	}
	
	/**
	 * Deliver online event to all friend listeners.
	 * @param username
	 */
	public void fireOnlineEvent (TVUser user) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveOnline(user);
	}
	
	/**
	 * Deliver offline event to all friend listeners.
	 * @param username
	 */
	public void fireOfflineEvent (TVUser user) {
		for (FWFriendListener listener:friend_listeners)
			listener.receiveOffline(user);
	}
	
	/**
	 * Check if user is online.
	 * @param jid
	 * @return
	 */
	public boolean isUserOnline (String jid) {
		return connection.getRoster().getPresence(jid).isAvailable();
	}
	
	/**
	 * Entry Classification Methods
	 */
	
	private boolean isFriendEntry (RosterEntry entry) {
		// both
		if (entry.getType() == RosterPacket.ItemType.both)
			return true;
		
		// from && subscribe
		if (entry.getType() == RosterPacket.ItemType.from &&
			entry.getStatus() == RosterPacket.ItemStatus.SUBSCRIPTION_PENDING)
			return true;
		
		return false;
	}
	
	private boolean isPendingEntry (RosterEntry entry) {
		// from && not subscribe yet
		if (entry != null &&
			entry.getType() == RosterPacket.ItemType.from &&
			entry.getStatus() != RosterPacket.ItemStatus.SUBSCRIPTION_PENDING &&
			!accepting_list.containsKey(entry.getUser()))
			return true;
		
		return false;
	}
	
	private boolean isAcceptingEntry (RosterEntry entry) {
		return accepting_list.containsKey(entry.getUser());
	}
	
	/**
	 * Listener for listening subscription presences.
	 * @author Kencool
	 *
	 */
	private class SubscriptionListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			Presence presence = (Presence) packet;
	       
	        if (presence.getType() == Presence.Type.subscribe) {
	        	processSubscribe(presence);
	        	
	        } else if (presence.getType() == Presence.Type.subscribed) {
	        	processSubscribed(presence);
	        } 
		}
	}
	
	/**
	 * Listener for listening roster entries' change event from XMPP connection.
	 * @author Kencool
	 *
	 */
	private class FWRosterListener implements RosterListener {
		
		@Override
		public void entriesAdded(Collection<String> addresses) {
			ArrayList<TVUser> friends = new ArrayList<TVUser>();
			for (String address:addresses) {
				RosterEntry entry = connection.getRoster().getEntry(address);
				
				// entry filter
				//
				if (entry.getUsername() == null || entry.getNickname() == null)
					continue;
				
				// build user
				//
				TVUser user = new TVUser();
				user.setUsername(entry.getUsername());
				user.setNickname(entry.getNickname());
				user.setBadgeId(entry.getBadgeId());
				user.setJid(entry.getUser());
				
				// handle user
				//
				if (isFriendEntry(entry)) {
					friends.add(user);
					
				} else if (isPendingEntry(entry)) {
					fireRequestEvent(user);
					
				} else if (isAcceptingEntry(entry)) {
					accepting_list.remove(entry.getUser());
					subscribeUser(entry.getUser());
				}
				
				user_list.put(address, user);
			}
			fireFriendEvent(friends);
		}

		@Override
		public void entriesDeleted(Collection<String> addresses) {
			ArrayList<TVUser> users = new ArrayList<TVUser>();
			for (String address:addresses) {
				TVUser user = user_list.get(address);
				if (user != null) {
					users.add(user);
					user_list.remove(address);
				}
			}
			fireRemoveEvent(users);
		}

		@Override
		public void entriesUpdated(Collection<String> addresses) {
			for (String address:addresses) {
				RosterEntry entry = connection.getRoster().getEntry(address);
				
				// entry filter
				//
				if (entry.getUsername() == null || entry.getNickname() == null)
					continue;
				if (entry.getType() == RosterPacket.ItemType.none)
					continue;
				
				// build user
				//
				TVUser user = new TVUser();
				user.setUsername(entry.getUsername());
				user.setNickname(entry.getNickname());
				user.setBadgeId(entry.getBadgeId());
				user.setJid(entry.getUser());
				
				// handle user
				//
				if (isFriendEntry(entry) && !user_list.containsKey(address)) {
					Presence presence = new Presence(Presence.Type.available);
					presence.setTo(entry.getUser());
					connection.sendPacket(presence);
				}
				
				user_list.put(address, user);
			}
		}

		@Override
		public void presenceChanged(Presence presence) {
			if (presence.isAvailable()) {
				TVUser user = user_list.get(StringUtils.parseBareAddress(presence.getFrom()));
				user.getExtraInfo().setMood(presence.getStatus());
				fireOnlineEvent(user);
			} else {
				Presence pres = connection.getRoster().getPresence(presence.getFrom());
				if (!pres.isAvailable()) {
					TVUser user = user_list.get(StringUtils.parseBareAddress(presence.getFrom()));
					fireOfflineEvent(user);
				}
			}
		}
	}
	
	public interface FWFriendListener {
		
		public void receiveFriends(ArrayList<TVUser> users);

		public void receiveRequest(TVUser user);
		
		public void receiveAccept(TVUser user);
		
		public void receiveRemoves(ArrayList<TVUser> users);
		
		public void receiveOnline(TVUser user);
		
		public void receiveOffline(TVUser user);
	}
}
