package com.wildmind.fanwave.xmpp.packet;

import org.jivesoftware.smack.packet.PacketExtension;

import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.util.StringGenerator;

public class FWMucExtension {
	
	public static class CommentExtension implements PacketExtension {
		/**
	     * The XML element name of an 'commentID' extension.
	     */
		public static final String ELEMENT_NAME = "commentID";
		
		/**
	     * The namespace that qualifies the XML element of an 'commentID' extension.
	     */
	    public static final String NAMESPACE = "jabber:client";
	    
	    private String comment_id;
	    
	    public CommentExtension (String id) {
	    	this.comment_id = id;
	    }
	    
	    public String getCommentId () {
	    	return comment_id;
	    }
	    
	    @Override
		public String getElementName() {
			return ELEMENT_NAME;
		}

		@Override
		public String getNamespace() {
			return NAMESPACE;
		}

		@Override
		public String toXML() {
			return "<" + ELEMENT_NAME + ">" + comment_id + "</" + ELEMENT_NAME + ">";
		}
	}
	
	public static class UserInfoExtension implements PacketExtension {
		/**
	     * The XML element name of an 'userinfo' extension.
	     */
		public static final String ELEMENT_NAME = "userInfo";
		
		/**
	     * The namespace that qualifies the XML element of an 'userinfo' extension.
	     */
	    public static final String NAMESPACE = "jabber:client";
		
		private TVUser user;
		
		public UserInfoExtension (TVUser user) {
			this.user = user;
		}
		
		public TVUser getUser () {
			return user;
		}
		
		@Override
		public String getElementName() {
			return ELEMENT_NAME;
		}

		@Override
		public String getNamespace() {
			return NAMESPACE;
		}

		@Override
		public String toXML() {
			String xml = "<" + ELEMENT_NAME + 
						 " username=\"" + user.getUsername() + "\"" +
						 " nickname=\"" + user.getNickname() + "\"" +
						 " mood=\"" + "\"" +
						 " createTime=\"" + StringGenerator.getCurrentTimeStringWithSeconds() + "\"" + 
						 " />";
			return xml;
		}
	}
	
	public static class ProgramInfoExtension implements PacketExtension {
		/**
	     * The XML element name of an 'programinfo' extension.
	     */
		public static final String ELEMENT_NAME = "programInfo";
		
		/**
	     * The namespace that qualifies the XML element of an 'programinfo' extension.
	     */
	    public static final String NAMESPACE = "jabber:client";
		
		private TVProgram program;
		
		public ProgramInfoExtension (TVProgram program) {
			this.program = program;
		}
		
		public TVProgram getProgram () {
			return program;
		}
		
		@Override
		public String getElementName() {
			return ELEMENT_NAME;
		}

		@Override
		public String getNamespace() {
			return NAMESPACE;
		}

		@Override
		public String toXML() {
			String xml = "<" + ELEMENT_NAME + 
					 " title=\"" + program.getTitle() + "\"" +
					 " sub_title=\"" + program.getSubTitle() + "\"" +
					 " pgid=\"" + program.getPgid() + "\"" +
					 " country=\"" + program.getCountry() + "\"" +
					 " channel=\"" + program.getChannelCode() + "\"" +
					 " start_at=\"" + program.getStartTime() + "\"" +
					 " end_at=\"" + program.getEndTime() + "\"" +
					 " />";
		return xml;
		}
	}
	
	public static class AttachmentExtension implements PacketExtension {
		/**
	     * The XML element name of an 'attachment' extension.
	     */
		public static final String ELEMENT_NAME = "attachment";
		
		/**
	     * The namespace that qualifies the XML element of an 'attachment' extension.
	     */
	    public static final String NAMESPACE = "jabber:client";
		
		private Attachment attach;
		
		public AttachmentExtension (Attachment attach) {
			this.attach = attach;
		}
		
		public Attachment getAttachment () {
			return attach;
		}
		
		@Override
		public String getElementName() {
			return ELEMENT_NAME;
		}

		@Override
		public String getNamespace() {
			return NAMESPACE;
		}

		@Override
		public String toXML() {
			String xml = "";
			
			if (attach.getType().equals(Attachment.ATTACH_IMAGE)) {
				xml = "<" + ELEMENT_NAME + 
						 " token=\"" + attach.getToken() + "\"" +
						 " type=\"" + attach.getType() + "\"" +
						 " desc=\"" + attach.getDescription() + "\"" +
						 " />";
			} else if (attach.getType().equals(Attachment.ATTACH_LINK)) {
				xml = "<" + ELEMENT_NAME + 
						 " token=\"" + attach.getToken() + "\"" +
						 " type=\"" + attach.getType() + "\"" +
						 " desc=\"" + attach.getDescription() + "\"" +
						 " url=\"" + attach.getUrl() + "\"" +
						 " />";
			}
			
			return xml;
		}
	}
}
