package protocolsupport.injector.pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.BungeeCord;
import raknetserver.RakNetServer;

public class PEProxyNetworkManager extends SimpleChannelInboundHandler<ByteBuf> {

	public static final String NAME = "peproxy-nm";

	protected Channel serverconnection;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof RakNetServer.BackPressure && serverconnection != null) {
			serverconnection.config().setAutoRead(RakNetServer.BackPressure.OFF.equals(msg));
			return;
		}
		super.channelRead(ctx, msg);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf bytebuf) {
		final ByteBuf cbytebuf = bytebuf.readRetainedSlice(bytebuf.readableBytes());
		if (serverconnection == null) {
			serverconnection = PEProxyServerConnection.connectToServer(ctx.channel(), cbytebuf);
		} else {
			serverconnection.writeAndFlush(cbytebuf)
                    .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		}
	}

	protected void closeServerConnection() {
		if (serverconnection != null) {
			serverconnection.disconnect().addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			serverconnection = null;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		closeServerConnection();
		super.channelInactive(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		closeServerConnection();
		super.channelUnregistered(ctx);
	}
}
