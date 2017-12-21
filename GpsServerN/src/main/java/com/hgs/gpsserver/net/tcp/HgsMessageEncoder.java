package com.hgs.gpsserver.net.tcp;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.hgs.common.utility.Byte2Hex;


public class HgsMessageEncoder extends ProtocolEncoderAdapter {
	private final Charset charset;

	public HgsMessageEncoder(Charset charset) {
		this.charset = charset;
	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		String res = (String) message;
		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
		buffer.put(Byte2Hex.HexString2Bytes(res));
		out.write(buffer);
	}
}
