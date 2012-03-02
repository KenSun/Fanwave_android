package com.wildmind.fanwave.xmpp.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.xmpp.packet.FWMucExtension;

public class CommentProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		String id = "";
		int parserDepth = parser.getDepth();
        while ( !(parser.next() == XmlPullParser.END_TAG && 
        		  parser.getDepth() == parserDepth) ) {
            id += parser.getText();
        }

		return new FWMucExtension.CommentExtension(id);
	}

}
