package protocolsupport.utils.netty;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;
import io.netty.util.Recycler.Handle;
import net.md_5.bungee.compress.CompressFactory;
import net.md_5.bungee.jni.zlib.BungeeZlib;

public class Compressor {

	private static final Recycler<Compressor> recycler = new Recycler<Compressor>() {
		@Override
		protected Compressor newObject(Handle<Compressor> handle) {
			return new Compressor(handle);
		}
	};

	public static Compressor create() {
		return recycler.get();
	}

	private final BungeeZlib zlib = CompressFactory.zlib.newInstance();
	private final Handle<Compressor> handle;

	protected Compressor(Handle<Compressor> handle) {
		this.handle = handle;
		zlib.init( true, Deflater.DEFAULT_COMPRESSION );
	}

	public void compress(ByteBuf in, ByteBuf out) {
		try {
			zlib.process(in, out);
		} catch (DataFormatException e) {
			throw new RuntimeException("Failed compressing packet", e);
		}
	}

	public void recycle() {
		handle.recycle(this);
	}

	@Override
	public void finalize() throws Throwable {
		zlib.free();
		super.finalize();
	}

}
