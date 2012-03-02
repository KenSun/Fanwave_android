package com.wildmind.fanwave.xmpp.provider;

import java.util.HashMap;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.xmpp.packet.FWExtend;
import com.wildmind.fanwave.xmpp.packet.InfoExtension.Info;

public class ExtendProvider implements PacketExtensionProvider {

	private static Info parseInfo (XmlPullParser parser) {
		HashMap<String, String> data = new HashMap<String, String>();
		
		int count = parser.getAttributeCount();
		for (int i = 0; i < count; i++)
			data.put(parser.getAttributeName(i), parser.getAttributeValue(i));
		
		return new Info(data);
	}
	
	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		String type = parser.getAttributeValue(null, "type");
		FWExtend extend = new FWExtend(type);
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				String elementName = parser.getName();
				if (elementName.equals("info")) {
					extend.addInfo(parseInfo(parser));
				}
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals(FWExtend.ELEMENT_NAME))
					done = true;
			}
		}
		
		return extend;
	}

}
