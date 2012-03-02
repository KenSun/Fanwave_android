package com.wildmind.fanwave.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

public class CommentManager {

	private TVProgram program = null;
	private CopyOnWriteArrayList<String> comment_id_orders = new CopyOnWriteArrayList<String>();
	private ConcurrentHashMap<String, Comment> comment_list = new ConcurrentHashMap<String, Comment>();
	private boolean more_comment = false;
	
	/**
	 * Constructor
	 * @param program
	 */
	public CommentManager (TVProgram program) {
		this.program = program;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		comment_id_orders.clear();
		comment_list.clear();
		more_comment = false;
	}
	
	/**
	 * Whether more comment available.
	 * @return
	 */
	public boolean isMoreComment () {
		return more_comment;
	}
	
	/**
	 * Get a comment by id.
	 * @param comment_id
	 * @return
	 */
	public Comment getComment (String comment_id) {
		return comment_list.get(comment_id);
	}
	
	/**
	 * Get ordered comments.
	 * @return
	 */
	public ArrayList<Comment> getCommentOrderedList () {
		ArrayList<Comment> list = new ArrayList<Comment>();
		for (String id:comment_id_orders)
			list.add(comment_list.get(id));
				
		return list;
	}
	
	/**
	 * Add a comment.
	 * @param comment
	 */
	public void addComment (Comment comment) {
		comment_id_orders.add(0, comment.getId());
		comment_list.put(comment.getId(), comment);
		
		if (comment_id_orders.size() > 20) {
			String id = comment_id_orders.get(comment_id_orders.size() - 1);
			comment_list.remove(id);
			comment_id_orders.remove(id);
			more_comment = true;
		}
	}
	
	/**
	 * Get comments.
	 * @param size
	 */
	public void getComments (int size) {
		getComments(null, size);
	}
	
	/**
	 * Get more comments
	 * @param size
	 */
	public void getMoreComments (int size) {
		if (!more_comment)
			return;
		getComments(comment_id_orders.get(comment_id_orders.size() - 1) , size);
	}
	
	/**
	 * Get previous comments from comment_id. If comment_id is null, get latest comments.
	 * @param comment_id
	 * @param size
	 */
	private void getComments (String comment_id, int size) {
		CopyOnWriteArrayList<String> id_orders = new CopyOnWriteArrayList<String>();
		ConcurrentHashMap<String, Comment> comments = new ConcurrentHashMap<String, Comment>();
		boolean more = false;
		
		// set http request URI, body
		//
		String URI = comment_id == null
				   ? NetworkManager.getBaseUrl() + "/comment/show"
				   : NetworkManager.getBaseUrl() + "/comment/show/page/" + comment_id + "/prev";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("title", program.getTitle());
		bodyMap.put("sub_title", program.getSubTitle());
		bodyMap.put("size", String.valueOf(size + 1));
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
				
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				more = len > size;
				for (int i = 0; i < len; i++) {
					if (i >= size)
						break;
					Comment cm = new Comment(array.getJSONObject(i));
					id_orders.add(cm.getId());
					comments.put(cm.getId(), cm);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		if (comment_id == null) {
			clear();
			comment_id_orders = id_orders;
			comment_list = comments;
		} else {
			comment_id_orders.addAll(id_orders);
			comment_list.putAll(comments);
		}
		more_comment = more;
	}
	
	/**
	 * Get user's latest uploaded image attachments.
	 * @param username
	 * @return ArrayList<Attachment>
	 */
	public static ArrayList<Attachment> getUserImageAttachment (String username) {
		ArrayList<Attachment> images = new ArrayList<Attachment>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/comment/attachment/image/user/" + 20 + "/show";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				JSONArray array = obj.getJSONArray("images");
				int len = array.length();
				for (int i = 0; i < len; i++)
					images.add(new Attachment(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return images;
	}
	
	/**
	 * Rate a comment. If chatJid is not null or zero length, rate by XMPP. Else by http request.
	 * @param chatJid
	 * @param comment_id
	 * @param is_like
	 * @return
	 */
	public static boolean rateComment(String chatJid, String comment_id, boolean is_like) {
		if (chatJid != null && chatJid.length() > 0)
			return FWXMPPManager.getMucManager().sendCommentRating(chatJid, comment_id, is_like);
		else 
			return rateComment(comment_id, is_like) == 200;
	}
	
	/**
	 * Rate a comment. Return the response code as integer. 
	 * @param comment_id
	 * @param is_like
	 * @param _context
	 * @return int		200 : success
	 * 					409 : already rated
	 * 					other: failure
	 */
	public static int rateComment (String comment_id, boolean is_like) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/comment/" + comment_id + "/rateComment";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("action", is_like ? "like" : "dislike");
		bodyMap.put("owner", AccountManager.getCurrentUser().getUsername());
		bodyMap.put("nickname", AccountManager.getCurrentUser().getNickname());

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return Integer.parseInt(response.get("code"));
	}
	
	/**
	 * Post attachment link.
	 * @param link_url
	 * @return Attachment or null if failed
	 */
	public static Attachment postLink (String link_url) {
		Attachment attach = null;
		
		// set http request URI, body
		// 
		String URI = NetworkManager.getBaseUrl() + "/comment/url/shortner";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("longUrl", link_url);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			attach = new Attachment();
			attach.setType(Attachment.ATTACH_LINK);
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				attach.setToken(obj.has("token") ? obj.getString("token") : "");
				attach.setUrl(obj.has("url") ? obj.getString("url") : "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return attach;
	}
	
	/**
	 * Post attachment image.
	 * @param filePath
	 * @return
	 */
	public static Attachment postImage (String filePath) {
		Attachment attach = null;
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/comment/image/upload";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.uploadImage(URI, filePath);
		
		if (response.get("code").equals("200")) {
			attach = new Attachment();
			attach.setType(Attachment.ATTACH_IMAGE);
			attach.setToken(response.get("data"));
		}
				
		return attach;
	}
	
	/**
	 * get comment info with id.
	 * @param id
	 * @return CommentInfo
	 */
	public static CommentInfo getCommentInfo (String id) {
		CommentInfo info = null;
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/comment/" + id + "/get";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				info = new CommentInfo(obj);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return info != null ? info : new CommentInfo();
	}
}
