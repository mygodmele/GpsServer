package com.hgs.gpsserver.net.tcp;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;


public class HgsProtocolCodecFactory implements ProtocolCodecFactory {

	private final HgsMessageEncoder encoder;
	private final HgsMessageDecoder decoder;

	public HgsProtocolCodecFactory() {
		this(Charset.defaultCharset());
	}

	public HgsProtocolCodecFactory(Charset charSet) {
		this.encoder = new HgsMessageEncoder(charSet);
		this.decoder = new HgsMessageDecoder(charSet);
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
}
