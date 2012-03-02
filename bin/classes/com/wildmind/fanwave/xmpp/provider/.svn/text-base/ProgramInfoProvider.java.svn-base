package com.wildmind.fanwave.xmpp.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.xmpp.packet.FWMucExtension;

public class ProgramInfoProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		String title = parser.getAttributeValue(null, "title");
		String sub_title = parser.getAttributeValue(null, "sub_title");
		String pgid = parser.getAttributeValue(null, "pgid");
		String country = parser.getAttributeValue(null, "country");
		String channel = parser.getAttributeValue(null, "channel");
		String start_time = parser.getAttributeValue(null, "start_at");
		String end_time = parser.getAttributeValue(null, "end_at");
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
		
		TVProgram program = new TVProgram();
		program.setTitle(title);
		program.setSubTitle(sub_title);
		program.setPgid(pgid);
		program.setCountry(country);
		program.setChannelCode(channel);
		program.setStartTime(start_time);
		program.setEndTime(end_time);
		
		return new FWMucExtension.ProgramInfoExtension(program);
	}

}
