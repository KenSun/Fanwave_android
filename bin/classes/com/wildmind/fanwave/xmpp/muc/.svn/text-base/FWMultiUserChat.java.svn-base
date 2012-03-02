package com.wildmind.fanwave.xmpp.muc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DataForm;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.xmpp.packet.FWExtend;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.AttachmentExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.CommentExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.UserInfoExtension;
import com.wildmind.fanwave.xmpp.packet.InfoExtension.Info;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

public class FWMultiUserChat extends MultiUserChat {

	public static final String MUC_CONFIG_REQUEST = "This room is locked from entry until configuration is confirmed.";
	public static final String MUC_UNLOCKED = "This room is now unlocked.";
	
	public static final String ERROR_409 = "HTTP/1.1 409 Conflict";
	public static final String ERROR_500 = "HTTP/1.1 500 Internal Server Error";
	
	private String 		room_jid;
	private TVProgram 	program;
	private MucPresenceInterceptor presence_interceptor = new MucPresenceInterceptor();
	private MucMessageInterceptor message_interceptor = new MucMessageInterceptor();
	private MucParticipantListener presence_listener = new MucParticipantListener();
	private MucMessageListener message_listener = new MucMessageListener();
	private FWMucExtension.UserInfoExtension userinfo_extension;
	private FWMucExtension.ProgramInfoExtension programinfo_extension;
	private CopyOnWriteArrayList<FWChatroomListener> chatroom_listeners = new CopyOnWriteArrayList<FWChatroomListener>();
	private ConcurrentHashMap<String, ArrayList<String>> user_resources = new ConcurrentHashMap<String, ArrayList<String>>();
	
	/**
	 * Constructor
	 * @param connection
	 * @param room_jid
	 * @param user
	 * @param program
	 */
	public FWMultiUserChat (XMPPConnection connection, String room_jid, TVUser user, TVProgram program) {
		super(connection, room_jid);
		
		this.room_jid = room_jid;
		this.program = program;
		this.userinfo_extension = new FWMucExtension.UserInfoExtension(user);
		this.programinfo_extension = new FWMucExtension.ProgramInfoExtension(program);
	}
	
	/**
	 * Get chat room listeners.
	 * @return
	 */
	public CopyOnWriteArrayList<FWChatroomListener> getChatroomListeners () {
		return chatroom_listeners;
	}
	
	/**
	 * Update all infos.
	 * @param user
	 * @param program
	 */
	public void updateInfos (TVUser user, TVProgram program) {
		this.program = program;
		this.userinfo_extension = new FWMucExtension.UserInfoExtension(user);
		this.programinfo_extension = new FWMucExtension.ProgramInfoExtension(program);
	}
	
	/**
	 * Update user info.
	 * @param user
	 */
	public void updateUserInfo (TVUser user) {
		this.userinfo_extension = new FWMucExtension.UserInfoExtension(user);
	}
	
	/**
	 * Update program info.
	 * @param program
	 */
	public void updateProgramInfo (TVProgram program) {
		this.program = program;
		this.programinfo_extension = new FWMucExtension.ProgramInfoExtension(program);
	}
	
	/**
	 * @Overloading
     * Joins the chat room using the specified nickname and password. If already joined
     * using another nickname, this method will first leave the room and then
     * re-join using the new nickname.<p>
     *
     * To control the amount of history to receive while joining a room you will need to provide
     * a configured DiscussionHistory object.<p>
     *
     * A password is required when joining password protected rooms. If the room does
     * not require a password there is no need to provide one.<p>
     *
     * If the room does not already exist when the user seeks to enter it, the server will
     * decide to create a new room or not.
     *
     * @param nickname the nickname to use.
     * @param password the password to use.
     * @param history the amount of discussion history to receive while joining a room.
     * @param timeout the amount of time to wait for a reply from the MUC service(in milleseconds).
     * @throws XMPPException if an error occurs joining the room. In particular, a
     *      401 error can occur if no password was provided and one is required; or a
     *      403 error can occur if the user is banned; or a
     *      404 error can occur if the room does not exist or is locked; or a
     *      407 error can occur if user is not on the member list; or a
     *      409 error can occur if someone is already in the group chat with the same nickname.
     */
	public synchronized void join () throws XMPPException {
		
		// If we've already joined the room, leave it before joining under a new nickname.
		//
		if (isJoined()) 
			leave();

		// add packet interceptors
		//
		addPresenceInterceptor(presence_interceptor);
		addMessageInterceptor(message_interceptor);
		addParticipantListener(presence_listener);
		addMessageListener(message_listener);
		
		// set max chars to 0 indicates we don't want any history
		//
		DiscussionHistory history = new DiscussionHistory();
		history.setMaxChars(0);
		
		super.join(FWXMPPManager.getUserFullJid(), null, history, 20000);
	}
	
	/**
	 * @Override
     * Leave the chat room.
     */
    public synchronized void leave() {
    	super.leave();
    	clear();
    }
    
    /**
     * Clear all data.
     */
    public void clear () {
    	user_resources.clear();
    	removePresenceInterceptor(presence_interceptor);
    	removeMessageInterceptor(message_interceptor);
    	removeParticipantListener(presence_listener);
    	removeMessageListener(message_listener);
    }
    
    public void reconnect () {
    	try {
    		leave();
			join();
			fireReconnectedEvent();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Send comment message.
     * @param text
     * @throws XMPPException 
     */
    public void sendComment (String text) throws XMPPException {
    	Message message = new Message(room_jid, Message.Type.groupchat);
        message.setBody(text);
        
        sendMessage(message);
    }
    
    /**
     * Send comment rating message.
     * @throws XMPPException 
     */
    public void sendPostRating (String comment_id, boolean is_like) throws XMPPException {
    	Message message = new Message(room_jid, Message.Type.groupchat);
    	FWExtend ext = new FWExtend(FWExtend.TYPE_POST_RATING);
    	
    	HashMap<String, String> data = new HashMap<String, String>();
    	data.put("username", AccountManager.getCurrentUser().getUsername());
    	data.put("commentID", comment_id);
    	data.put("ratingType", is_like ? "like" : "dislike");
    	ext.addInfo(data);
    	message.addExtension(ext);
    	
        sendMessage(message);
    }
    
    /**
     * Send attachment message.
     * @param attach
     * @throws XMPPException
     */
    public void sendAttachment (Attachment attach) throws XMPPException {
    	Message message = new Message(room_jid, Message.Type.groupchat);
        message.setBody(attach.getDescription());
        
        AttachmentExtension ext = new AttachmentExtension(attach);
        message.addExtension(ext);
        
        sendMessage(message);
    }

    /**
     * Send image attachment message.
     * @param description
     * @param token
     * @throws XMPPException
     */
    public void sendAttachImage (String description, String token) throws XMPPException {
    	Message message = new Message(room_jid, Message.Type.groupchat);
        message.setBody(description);
        
        Attachment attach = new Attachment(Attachment.ATTACH_IMAGE, token, "", description, "");
        AttachmentExtension ext = new AttachmentExtension(attach);
        message.addExtension(ext);
        
        sendMessage(message);
    }
    
    /**
     * Send link attachment message.
     * @param description
     * @param token
     * @param url
     * @throws XMPPException
     */
    public void sendAttachLink (String description, String token, String url) throws XMPPException {
    	Message message = new Message(room_jid, Message.Type.groupchat);
        message.setBody(description);
        
        Attachment attach = new Attachment(Attachment.ATTACH_LINK, token, url, description, "");
        AttachmentExtension ext = new AttachmentExtension(attach);
        message.addExtension(ext);
        
        sendMessage(message);
    }
    
    /**
     * Add chat room listeners.
     * @param listeners
     */
    public void addChatroomListeners (CopyOnWriteArrayList<FWChatroomListener> listeners) {
    	chatroom_listeners.addAll(listeners);
    }
    
    /**
     * Add a chat room listener.
     * @param listener
     */
    public void addChatroomListener (FWChatroomListener listener) {
    	chatroom_listeners.add(listener);
    }
    
    /**
     * Remove a chat room listener.
     * @param listener
     */
    public void removeChatroomListener (FWChatroomListener listener) {
    	chatroom_listeners.remove(listener);
    }
    
    private void handlePresence (Presence presence) {
    	UserInfoExtension user_ext = (UserInfoExtension) presence.getExtension( UserInfoExtension.ELEMENT_NAME,
					UserInfoExtension.NAMESPACE);
    	TVUser user = user_ext.getUser();
    	
    	ArrayList<String> resources = user_resources.get(user.getUsername());
    	
    	// handle available presence
    	if (presence.getType() == Type.available) {
    		boolean join = false;
    		if (resources == null || resources.size() == 0) {
				resources = new ArrayList<String>();
				user_resources.put(user.getUsername(), resources);
				join = true;
			}
			if (!resources.contains(presence.getFrom()))
				resources.add(presence.getFrom());	

			if (join)
				fireUserJoinedEvent(user);
			
    	} 
    	// handle unavailable presence
    	else if (presence.getType() == Type.unavailable) {
    		if (resources != null) {
				resources.remove(presence.getFrom());
				if (resources.size() == 0) {
					user_resources.remove(user.getUsername());
					fireUserLeftEvent(user);
				}
			}
    	}
    }
    
    /**
     * Handle a message.
     * @param message
     */
    private void handleMessage (Message message) {
    	FWMessageType type = getMessageType(message);
    	
    	if (type == FWMessageType.origin) {
    		if (message.getBody().equals(MUC_CONFIG_REQUEST)) 
    			confirmConfiguration();
    		
    	} else if (type == FWMessageType.comment) {
    		handleComment(message);
    		
    	} else if (type == FWMessageType.postRating) {
    		handlePostRating(message);
    		
    	} else if (type == FWMessageType.attachment) {
    		handleAttachment(message);
    	} else {
    	}
    }
    
    /**
     * Send MUC configuration.
     * Set max users count to 0, which means no limit.
     * Set room description to pgid.
     */
    private void confirmConfiguration () {
    	DataForm dataForm = new DataForm(Form.TYPE_SUBMIT);
    	FormField field;
    	
    	field = new FormField("muc#roomconfig_maxusers");
    	field.addValue("0");
    	dataForm.addField(field);
    	
    	field = new FormField("muc#roomconfig_roomdesc");
    	field.addValue(program.getPgid());
    	dataForm.addField(field);
    	
    	try {
			sendConfigurationForm(new Form(dataForm));
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Handle a comment.
     * @param message
     */
    private void handleComment (Message message) {
    	CommentExtension cm_ext = (CommentExtension) message.getExtension( CommentExtension.ELEMENT_NAME, 
				  									 					   CommentExtension.NAMESPACE );
    	UserInfoExtension user_ext = (UserInfoExtension) message.getExtension( UserInfoExtension.ELEMENT_NAME,
    																		   UserInfoExtension.NAMESPACE);
    	TVUser cm_user = user_ext.getUser();
    	if (message.getError() != null)
    		handleCommentError(cm_user, message.getError());
    	else {
        	Comment cm = new Comment(cm_ext.getCommentId(), cm_user.getUsername(), cm_user.getNickname(),
        							 message.getBody());
        	fireCommentEvent(cm);
    	}
    }
    
    private void handleCommentError (TVUser user, XMPPError error) {
    	if (!user.getUsername().equals(AccountManager.getCurrentUser().getUsername()))
    		return;
    	
    	String errMsg = error.getContent();
    	fireCommentErrorEvent(errMsg);
    }
    
    /**
     * Handle post rating message.
     * @param message
     */
    private void handlePostRating (Message message) {
    	FWExtend extend = (FWExtend) message.getExtension( FWExtend.ELEMENT_NAME,
		 		   										   FWExtend.NAMESPACE );
    	Info info = extend.getInfo();
    	if (info == null)
    		return;
    	
    	if (message.getError() != null)
    		handlePostRatingError(info, message.getError());
    	else {
    		String commentId = info.getValue("commentID");
    		String ratingType = info.getValue("ratingType");
    		String username = info.getValue("username");
    		
    		firePostRatingEvent(commentId, ratingType, username);
    	}
    }
    
    /**
     * Handle post rating error message. Currently handle post conflict only.
     * @param info
     * @param error
     */
    private void handlePostRatingError (Info info, XMPPError error) {
    	String username = info.getValue("username");
    	String commentId = info.getValue("commentID");
    	if (!username.equals(AccountManager.getCurrentUser().getUsername()))
    		return;
    	
    	String errMsg = error.getContent();
    	if (errMsg.equals(ERROR_409))
    		firePostRatingFailureEvent(commentId, errMsg);
    }
    
    /**
     * Handle attachment message.
     * @param message
     */
    private void handleAttachment (Message message) {
    	CommentExtension cm_ext = (CommentExtension) message.getExtension( CommentExtension.ELEMENT_NAME, 
				   														   CommentExtension.NAMESPACE );
    	AttachmentExtension ext = (AttachmentExtension) message.getExtension( AttachmentExtension.ELEMENT_NAME,
				 															  AttachmentExtension.NAMESPACE );
    	UserInfoExtension user_ext = (UserInfoExtension) message.getExtension( UserInfoExtension.ELEMENT_NAME,
				   															   UserInfoExtension.NAMESPACE);
    	TVUser cm_user = user_ext.getUser();
    	Attachment attach = ext.getAttachment();
    	
    	Comment cm = new Comment(cm_ext.getCommentId(), cm_user.getUsername(), cm_user.getNickname(), 
    							 message.getBody());
    	cm.addAttach(attach);
    	
    	fireAttachmentEvent(cm);
    }
    
    private void fireUserJoinedEvent (TVUser user) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receiveUserJoined(user);
    }
    
    private void fireUserLeftEvent (TVUser user) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receiveUserLeft(user);
    }
    
    private void fireCommentEvent (Comment comment) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receiveComment(comment);
    }
    
    private void fireCommentErrorEvent (String errMsg) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receiveCommentFailure(errMsg);
    }
    
    private void firePostRatingEvent (String comment_id, String rating_type, String username) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receivePostRating(comment_id, username, rating_type.equals("like"));
    }
    
    private void firePostRatingFailureEvent (String commentId, String errMsg) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receivePostRatingFailure(commentId, errMsg);
    }
    
    private void fireAttachmentEvent (Comment comment) {
    	for (FWChatroomListener listener:chatroom_listeners)
    		listener.receiveAttachment(comment);
    }
    
    private void fireReconnectedEvent () {
    	for (FWChatroomListener listener:chatroom_listeners) {
    		if (listener != null)
    			listener.chatroomReconnected();
    	}
    }

	/**
     * MUCPresenceInterceptor class
     * Intercept each outgoing presence packet and add self-defined extensions.
     * @author Kencool
     *
     */
	private class MucPresenceInterceptor implements PacketInterceptor {
		@Override
		public void interceptPacket(Packet packet) {
			packet.addExtension(userinfo_extension);
			packet.addExtension(programinfo_extension);
		}
	}
	
	/**
	 * MucMessageInterceptor class
	 * Intercept each outgoing message packet and add self-defined extensions.
	 * @author Kencool
	 *
	 */
	private class MucMessageInterceptor implements PacketInterceptor {

		@Override
		public void interceptPacket(Packet packet) {
			packet.addExtension(userinfo_extension);
			packet.addExtension(programinfo_extension);
		}
	}
	
	/**
	 * MucParticipantListener class
	 * @author Kencool
	 *
	 */
	private class MucParticipantListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			Presence presence = (Presence) packet;
			handlePresence(presence);
		}
	}
	
	/**
	 * MucMessageListener class
	 * @author Kencool
	 *
	 */
	private class MucMessageListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			Message message = (Message) packet;
			handleMessage(message);
		}
	}
	
	/**
	 * Fanwave message types.
	 * @author Kencool
	 *
	 */
	public static enum FWMessageType {
		/**
		 * XMPP original message.
		 */
		origin,
		/**
		 * User comment message.
		 */
		comment,
		/**
		 * Comment rating message.
		 */
		postRating,
		/**
		 * Image attachment message.
		 */
		attachment,
		/**
		 * Unknown message.
		 */
		unknown
	}
	
	/**
	 * Get Fanwave message type of a message.
	 * @param message
	 * @return
	 */
	public static FWMessageType getMessageType (Message message) {
		
		if (isOriginMessage(message))
			return FWMessageType.origin;
		
		else if (isCommentMessage(message))
			return FWMessageType.comment;
		
		else if (isPostRatingMessage(message))
			return FWMessageType.postRating;
		
		else if (isAttachmentMessage(message))
			return FWMessageType.attachment;
		
		else 
			return FWMessageType.unknown;
	}
	
	/**
	 * Check if is XMPP original message.
	 * @param message
	 * @return
	 */
	public static boolean isOriginMessage (Message message) {
		return message.getExtensions().size() == 0;
	}
	
	/**
	 * Check if is comment message.
	 * @param message
	 * @return
	 */
	public static boolean isCommentMessage (Message message) {
		return !isAttachmentMessage(message) &&
				message.getExtension( CommentExtension.ELEMENT_NAME, 
									  CommentExtension.NAMESPACE ) != null;
	}
	
	/**
	 * Check if is post rating message.
	 * @param message
	 * @return
	 */
	public static boolean isPostRatingMessage (Message message) {
		FWExtend extend = (FWExtend) message.getExtension( FWExtend.ELEMENT_NAME,
												 		   FWExtend.NAMESPACE );
		if (extend == null)
			return false;
		
		return extend.getType().equals(FWExtend.TYPE_POST_RATING);	
	}
	
	/**
	 * Check if is attachment message.
	 * @param message
	 * @return
	 */
	public static boolean isAttachmentMessage (Message message) {
		return message.getExtension( AttachmentExtension.ELEMENT_NAME,
									 AttachmentExtension.NAMESPACE ) != null;
	}
}
