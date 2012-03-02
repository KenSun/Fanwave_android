package com.wildmind.fanwave.xmpp.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension;

public class AttachmentProvider implements PacketExtensionProvider {

	private static Attachment parseAttachImage (XmlPullParser parser) {
		String token = parser.getAttributeValue(null, "token");
		String descr = parser.getAttributeValue(null, "desc");
		
		Attachment attach = new Attachment(Attachment.ATTACH_IMAGE);
		attach.setToken(token);
		attach.setDescription(descr);
		
		return attach;
	}
	
	private static Attachment parseAttachLink (XmlPullParser parser) {
		String token = parser.getAttributeValue(null, "token");
		String descr = parser.getAttributeValue(null, "desc");
		String url = parser.getAttributeValue(null, "url");
		
		Attachment attach = new Attachment(Attachment.ATTACH_LINK);
		attach.setToken(token);
		attach.setDescription(descr);
		attach.setUrl(url);
		
		return attach;
	}
	
	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		String type = parser.getAttributeValue(null, "type");
		
		Attachment attach;
		if (type.equals(Attachment.ATTACH_IMAGE))
			attach = parseAttachImage(parser);
		else if (type.equals(Attachment.ATTACH_LINK))
			attach = parseAttachLink(parser);
		else
			attach = new Attachment();
		
		return new FWMucExtension.AttachmentExtension(attach);
	}
}
