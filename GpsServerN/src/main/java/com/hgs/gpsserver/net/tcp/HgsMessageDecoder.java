package com.hgs.gpsserver.net.tcp;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;

import com.hgs.common.utility.Byte2Hex;
import com.hgs.gpsserver.common.Constants;
import com.hgs.gpsserver.module.NetModule;

public class HgsMessageDecoder extends CumulativeProtocolDecoder {
	private final Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

	private final static Logger LOGGER = NetModule.instance.getLogger();

	public HgsMessageDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		Context ctx = getContext(session);
		IoBuffer buffer = ctx.innerBuffer;
		boolean isNeedConvert = ctx.isNeedConvert();
		int matchCount = ctx.getMatchCount();
		
		while(in.hasRemaining()) {
			byte b = in.get();
			if(!isNeedConvert) {
				if(b != 0x7d) {
					buffer.put(b);
				}
			} else {
				if(b == 0x02) {
					buffer.put((byte) 0x7e);
				} else if(b == 0x01) {
					buffer.put((byte) 0x7d);
				}
			}
			if(b == 0x7d) {
				isNeedConvert = true;
			} else {
				isNeedConvert = false;
			}
			ctx.setNeedConvert(isNeedConvert);
			
			if(b == 0x7e && matchCount > 5) {
				buffer.flip();
				byte[] message = new byte[buffer.limit()];
				buffer.get(message);
				out.write(message);
				ctx.reset();
				LOGGER.debug("Delimited message=" + Byte2Hex.Bytes2HexString(message));
				return true;
			}
			matchCount++;
			ctx.setMatchCount(matchCount);
		}
		return false;
	}

	private Context getContext(IoSession session) {
		Context context = (Context) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new Context();
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}

	private class Context {
		private final IoBuffer innerBuffer;
		private int matchCount = 0;
		private boolean isNeedConvert = false;
		
		public boolean isNeedConvert() {
			return isNeedConvert;
		}

		public void setNeedConvert(boolean isNeedConvert) {
			this.isNeedConvert = isNeedConvert;
		}

		public int getMatchCount() {
			return matchCount;
		}

		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}
		
		public Context() {
			innerBuffer = IoBuffer.allocate(100).setAutoExpand(true);
		}

		public void reset() {
			this.innerBuffer.clear();
			this.matchCount = 0;
			this.isNeedConvert = false;
		}
	}
}
