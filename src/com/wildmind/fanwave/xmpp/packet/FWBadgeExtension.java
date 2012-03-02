package com.wildmind.fanwave.xmpp.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public class FWBadgeExtension {

	public static class BadgeExtension implements PacketExtension {
		/**
	     * The XML element name of an 'badgeID' extension.
	     */
		public static final String ELEMENT_NAME = "badgeID";
		
		/**
	     * The namespace that qualifies the XML element of an 'badgeID' extension.
	     */
	    public static final String NAMESPACE = "jabber:client";
	    
	    private String badge_id;
	    
	    public BadgeExtension (String id) {
	    	this.badge_id = id;
	    }
	    
	    public String getBadgeId () {
	    	return badge_id;
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
			return "<" + ELEMENT_NAME + ">" + badge_id + "</" + ELEMENT_NAME + ">";
		}
		
	}
}
