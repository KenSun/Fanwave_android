package com.wildmind.fanwave.xmpp.muc;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;

import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.xmpp.packet.FWExtend;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.AttachmentExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.CommentExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.ProgramInfoExtension;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension.UserInfoExtension;
import com.wildmind.fanwave.xmpp.provider.AttachmentProvider;
import com.wildmind.fanwave.xmpp.provider.CommentProvider;
import com.wildmind.fanwave.xmpp.provider.ExtendProvider;
import com.wildmind.fanwave.xmpp.provider.ProgramInfoProvider;
import com.wildmind.fanwave.xmpp.provider.UserInfoProvider;

public class FWMucManager {
	
	static {
		ProviderManager.getInstance().addExtensionProvider(UserInfoExtension.ELEMENT_NAME,
				UserInfoExtension.NAMESPACE, new UserInfoProvider());
		ProviderManager.getInstance().addExtensionProvider(ProgramInfoExtension.ELEMENT_NAME,
				ProgramInfoExtension.NAMESPACE, new ProgramInfoProvider());
		ProviderManager.getInstance().addExtensionProvider(CommentExtension.ELEMENT_NAME,
				CommentExtension.NAMESPACE, new CommentProvider());
		ProviderManager.getInstance().addExtensionProvider(AttachmentExtension.ELEMENT_NAME,
				AttachmentExtension.NAMESPACE, new AttachmentProvider());
		ProviderManager.getInstance().addExtensionProvider(FWExtend.ELEMENT_NAME,
				FWExtend.NAMESPACE, new ExtendProvider());
	}

	private XMPPConnection connection;
	private ConcurrentHashMap<String, FWMultiUserChat> muc_list = new ConcurrentHashMap<String, FWMultiUserChat>();
	
	// constructor
	//
	public FWMucManager (XMPPConnection connection) {
		this.connection = connection;
	}
	
	/**
	 * Clean up resources of this muc manager.
	 */
	public void clear () {
		muc_list.clear();
		muc_list = null;
		connection = null;
	}
	
	/**
	 * Add a chat room listener.
	 * @param chatJid
	 * @param listener
	 */
	public void addChatroomListener (String chatJid, FWChatroomListener listener) {
		if (chatJid == null || chatJid.length() == 0 || 
			listener == null || !muc_list.containsKey(chatJid))
			return;
		
		FWMultiUserChat muc = muc_list.get(chatJid);
		
		// remove listener for avoiding duplicate
		muc.removeChatroomListener(listener);
		muc.addChatroomListener(listener);
	}
	
	/**
	 * Remove a chat room listener.
	 * @param chatJid
	 * @param listener
	 */
	public void removeChatroomListener (String chatJid, FWChatroomListener listener) {
		if (chatJid == null || chatJid.length() == 0 || 
			listener == null || !muc_list.containsKey(chatJid))
				return;
		FWMultiUserChat muc = muc_list.get(chatJid);
		muc.removeChatroomListener(listener);
	}
	
	/**
	 * Build a muc.
	 * @param chatJid
	 * @param user
	 * @param program
	 * @param listener
	 * @return boolean false if muc of chatJid already exists
	 */
	public boolean buildMuc (String chatJid, TVUser user, TVProgram program, FWChatroomListener listener) {
		if (chatJid == null || chatJid.length() == 0)
			return false;
		
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid)) {
			FWMultiUserChat muc = new FWMultiUserChat(connection, chatJid, user, program);
			muc_list.put(chatJid, muc);
			if (listener != null)
				muc.addChatroomListener(listener);
			success = true;
		}
		
		return success;
	}
	
	/**
	 * Join muc of chatJid
	 * @param chatJid
	 * @return boolean false if failed or muc of chatJid doesn't exist
	 */
	public boolean joinMUC (String chatJid) {
		if (chatJid == null || chatJid.length() == 0 || !muc_list.containsKey(chatJid))
			return false;
		
		FWMultiUserChat muc = muc_list.get(chatJid);
		if (muc.isJoined())
			return true;
		
		boolean success = false;
		// clear muc data before join
		muc.clear();
		try {
			muc.join();
			success = true;
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Join muc of chatJid. If muc doesn't exist, create it.
	 * @param chatJid
	 * @param user
	 * @param program
	 * @param listener
	 * @return boolean false if failed
	 */
	public boolean joinMUC (String chatJid, TVUser user, TVProgram program, FWChatroomListener listener) {
		if (chatJid == null || chatJid.length() == 0)
			return false;
		
		boolean success = false;
		FWMultiUserChat muc;
		
		if (muc_list.containsKey(chatJid))
			muc = muc_list.get(chatJid);
		else {
			muc = new FWMultiUserChat(connection, chatJid, user, program);
			muc_list.put(chatJid, muc);
		}
		
		if (listener != null) {
			// remove listener for avoiding duplicate
			muc.removeChatroomListener(listener);
			muc.addChatroomListener(listener);
		}
	
		try {
			muc.join();
			success = true;
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Leave muc of chatJid. This method only leaves muc but destroy it.
	 * @param chatJid
	 * @param listener
	 */
	public void leaveMUC (String chatJid, FWChatroomListener listener) {
		if (chatJid == null || !muc_list.containsKey(chatJid))
			return;
		
		FWMultiUserChat muc = muc_list.get(chatJid);
		muc.removeChatroomListener(listener);
		if (muc.getChatroomListeners().size() == 0)
			muc.leave();
	}
	
	/**
	 * Destroy muc of chatJid.
	 * @param chatJid
	 * @param listener
	 */
	public void closeMUC (String chatJid, FWChatroomListener listener) {
		if (chatJid == null || !muc_list.containsKey(chatJid))
			return;
		
		leaveMUC(chatJid, listener);
		muc_list.remove(chatJid);
	}
	
	/**
	 * Reconnect all mucs.
	 */
	public void reconnectMucs () {
		Set<String> set = muc_list.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			muc_list.get(key).reconnect();
		}
	}
	
	/**
	 * Check whether is joined muc of chatJid.
	 * @param chatJid
	 * @return
	 */
	public boolean isJoinedMUC (String chatJid) {
		if (chatJid == null || ! muc_list.containsKey(chatJid))
			return false;
		
		return muc_list.get(chatJid).isJoined();
	}
	
	/**
	 * Send comment to muc of chatJid.
	 * @param chatJid
	 * @param text
	 * @return
	 */
	public boolean sendComment (String chatJid, String text) {
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid))
			return false;
		
		try {
			muc_list.get(chatJid).sendComment(text);
			success = true;
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Send attach to muc of chatJid.
	 * @param chatJid
	 * @param attach
	 * @return
	 */
	public boolean sendAttachment (String chatJid, Attachment attach) {
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid))
			return false;
		
		try {
			muc_list.get(chatJid).sendAttachment(attach);
			success = true;
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Send attach link to muc of chatJid.
	 * @param chatJid
	 * @param description
	 * @param token
	 * @param url
	 * @return
	 */
	public boolean sendAttachLink (String chatJid, String description, String token, String url) {
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid))
			return false;
		
		try {
			muc_list.get(chatJid).sendAttachLink(description, token, url);
			success = true;
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Send attach image to muc of chatJid.
	 * @param chatJid
	 * @param description
	 * @param token
	 * @return
	 */
	public boolean sendAttachImage (String chatJid, String description, String token) {
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid))
			return false;
		
		try {
			muc_list.get(chatJid).sendAttachImage(description, token);
			success = true;
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Rate a comment in muc of chatJid.
	 * @param chatJid
	 * @param comment_id
	 * @param is_like
	 * @return
	 */
	public boolean sendCommentRating (String chatJid, String comment_id, boolean is_like) {
		boolean success = false;
		
		if (!muc_list.containsKey(chatJid))
			return false;
		
		try {
			muc_list.get(chatJid).sendPostRating(comment_id, is_like);
			success = true;
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return success;
	}
}
