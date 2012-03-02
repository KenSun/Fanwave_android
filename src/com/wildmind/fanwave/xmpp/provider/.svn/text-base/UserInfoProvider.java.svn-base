package com.wildmind.fanwave.xmpp.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension;

public class UserInfoProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		String username = parser.getAttributeValue(null, "username");
		String nickname = parser.getAttributeValue(null, "nickname");
		String mood = parser.getAttributeValue(null, "mood");
		
		TVUser user = new TVUser();
		user.setUsername(username);
		user.setNickname(nickname);
		user.getExtraInfo().setMood(mood);
		
		return new FWMucExtension.UserInfoExtension(user);
	}

}
