package com.wildmind.fanwave.xmpp.muc;

import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.user.TVUser;

public interface FWChatroomListener {
	
	public void receiveUserJoined(TVUser user);
	
	public void receiveUserLeft(TVUser user);
	
	public void receiveComment(Comment comment);
	
	public void receiveCommentFailure(String err_msg);
	
	public void receivePostRating(String comment_id, String username, boolean is_like);
	
	public void receivePostRatingFailure(String comment_id, String err_msg);
	
	public void receiveAttachment(Comment comment);
	
	public void chatroomReconnected();
}
