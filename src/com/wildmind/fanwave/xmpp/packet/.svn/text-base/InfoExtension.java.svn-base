package com.wildmind.fanwave.xmpp.packet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jivesoftware.smack.packet.PacketExtension;

public class InfoExtension implements PacketExtension {

	/**
     * The XML element name of an 'info' extension.
     */
	public static final String ELEMENT_NAME = "info";
	
	/**
     * The namespace that qualifies the XML element of an 'info' extension.
     */
    public static final String NAMESPACE = "jabber:client";
    
    private Info info;
    
    public InfoExtension (HashMap<String, String> data) {
    	this.info = new Info(data);
    }
    
    public InfoExtension (Info info) {
    	this.info = info;
    }
    
    public Info getInfo () {
    	return info;
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
		return info.toXML();
	}
	
	/**
	 * Represents an info.
	 * @author Kencool
	 *
	 */
	public static class Info {
		
		private HashMap<String, String> info;
		
		public Info (HashMap<String, String> info) {
			this.info = info;
		}
		
		public HashMap<String, String> getInfo () {
			return info;
		}
		
		public Set<String> getNames () {
			return info.keySet();
		}
		
		public String getValue (String name) {
			return info.get(name);
		}
		
		public Collection<String> getValues () {
			return info.values();
		}
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<info");
			Set<String> set = info.keySet();
			Iterator<String> keys = set.iterator();
			while (keys.hasNext()) {
				String key = keys.next().toString();
				buf.append(" " + key + "=\"").append(info.get(key).toString() + "\"");
			}
			buf.append(" />");
			
			return buf.toString();
		}
	}
}
