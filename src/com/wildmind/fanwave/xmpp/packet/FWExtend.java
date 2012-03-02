package com.wildmind.fanwave.xmpp.packet;

import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

import com.wildmind.fanwave.xmpp.packet.InfoExtension.Info;

public class FWExtend extends Packet implements PacketExtension {

	public static final String TYPE_POST_RATING = "commentRating";
	
	/**
     * The XML element name of an 'extend' extension.
     */
	public static final String ELEMENT_NAME = "extend";
	
	/**
     * The namespace that qualifies the XML element of an 'extend' extension.
     */
    public static final String NAMESPACE = "jabber:client";
    
    private String type;
    private ArrayList<Info> infos = new ArrayList<Info>();
    
    // constructor
    //
    public FWExtend (String type) {
    	this.type = type;
    }
    
    public FWExtend (String type, HashMap<String, String> data) {
    	this.type = type;
    	this.infos.add(new Info(data));
    }
    
    public FWExtend (String type, Info info) {
    	this.type = type;
    	this.infos.add(info);
    }
    
    public FWExtend (String type, ArrayList<Info> infos) {
    	this.type = type;
    	this.infos = infos;
    }
    
    public String getType () {
    	return type;
    }
    
    public Info getInfo () {
    	return infos.size() > 0 ? infos.get(0) : null;
    }
    
    public ArrayList<Info> getInfos () {
    	return infos;
    }
    
    public void addInfo (HashMap<String, String> data) {
    	if (data == null || data.size() == 0)
    		return;
    	
    	infos.add(new Info(data));
    }
    
    public void addInfo (Info info) {
    	if (info == null)
    		return;
    	
    	infos.add(info);
    }
    
    public boolean removeInfo (Info info) {
    	return infos.remove(info);
    }
    
    public void clearInfos () {
    	infos.clear();
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
		StringBuilder buf = new StringBuilder();
		buf.append("<extend");
		if (type != null)
			buf.append(" type=\"").append(type);
		buf.append("\">");
		
		// add infos
		for (Info info:infos)
			buf.append(info.toXML());
		
		buf.append("</extend>");
		
		return buf.toString();
	}
}
