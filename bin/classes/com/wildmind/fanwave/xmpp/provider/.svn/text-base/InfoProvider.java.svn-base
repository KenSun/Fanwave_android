package com.wildmind.fanwave.xmpp.provider;

import java.util.HashMap;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.xmpp.packet.InfoExtension;

public class InfoProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		HashMap<String, String> data = new HashMap<String, String>();
		
		int count = parser.getAttributeCount();
		for (int i = 0; i < count; i++)
			data.put(parser.getAttributeName(i), parser.getAttributeValue(i));
		
		return new InfoExtension(data);
	}

}
