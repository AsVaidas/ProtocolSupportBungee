package protocolsupport.protocol.packet.middleimpl.writeable.play.v_pe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.Login;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middle.WriteableMiddlePacket;
import protocolsupport.protocol.serializer.PEPacketIdSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.utils.netty.Allocator;

public class StartGamePacket extends WriteableMiddlePacket<Login> {

	private static final String levelId = UUID.randomUUID().toString();

	@Override
	public Collection<ByteBuf> toData(Login packet) {
		ArrayList<ByteBuf> packets = new ArrayList<>();
		ByteBuf resourcepack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcepack, PEPacketId.Clientbound.PLAY_RESOURCE_PACK);
		resourcepack.writeBoolean(false); // required
		resourcepack.writeShortLE(0); //beh packs count
		resourcepack.writeShortLE(0); //res packs count
		packets.add(resourcepack);
		ByteBuf resourcestack = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(resourcestack, PEPacketId.Clientbound.PLAY_RESOURCE_STACK);
		resourcestack.writeBoolean(false); // required
		VarNumberSerializer.writeVarInt(resourcestack, 0); //beh packs count
		VarNumberSerializer.writeVarInt(resourcestack, 0); //res packs count
		packets.add(resourcestack);
		ByteBuf startgame = Allocator.allocateBuffer();
		PEPacketIdSerializer.writePacketId(startgame, PEPacketId.Clientbound.PLAY_START_GAME);
		VarNumberSerializer.writeSVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeVarLong(startgame, packet.getEntityId());
		VarNumberSerializer.writeSVarInt(startgame, packet.getGameMode());
		startgame.writeFloatLE(0); //x
		startgame.writeFloatLE(0); //y
		startgame.writeFloatLE(0); //z
		startgame.writeFloatLE(0); //yaw
		startgame.writeFloatLE(0); //pitch
		VarNumberSerializer.writeSVarInt(startgame, 0); //seed
		VarNumberSerializer.writeSVarInt(startgame, RespawnPacket.getPeDimensionId(packet.getDimension()));
		VarNumberSerializer.writeSVarInt(startgame, 1); //world type (1 - infinite)
		VarNumberSerializer.writeSVarInt(startgame, 0); // world gamemode (SURVIVAL)
		VarNumberSerializer.writeSVarInt(startgame, packet.getDifficulty());
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn x
		VarNumberSerializer.writeVarInt(startgame, 0); //world spawn y
		VarNumberSerializer.writeSVarInt(startgame, 0); //world spawn z
		startgame.writeBoolean(false); //disable achievements
		VarNumberSerializer.writeSVarInt(startgame, 0); //time
		startgame.writeBoolean(false); //edu mode
		startgame.writeBoolean(false); //edu mode
		startgame.writeFloatLE(0); //rain level
		startgame.writeFloatLE(0); //lighting level
		startgame.writeBoolean(true); //is multiplayer
		startgame.writeBoolean(false); //broadcast to lan
		startgame.writeBoolean(false); //broadcast to xbl
		startgame.writeBoolean(true); //commands enabled
		startgame.writeBoolean(false); //needs texture pack
		VarNumberSerializer.writeVarInt(startgame, 1); //game rules
		StringSerializer.writeVarIntUTF8String(startgame, "dodaylightcycle");
		VarNumberSerializer.writeVarInt(startgame, 1); //game rules bool
		startgame.writeBoolean(false);
		startgame.writeBoolean(false); //player map enabled
		startgame.writeBoolean(false); //trust players
		VarNumberSerializer.writeSVarInt(startgame, 1); //permission level
		VarNumberSerializer.writeSVarInt(startgame, 4); //game publish setting
		startgame.writeIntLE(4); //chunk tick radius
		startgame.writeBoolean(false); //Broadcast intent
		startgame.writeBoolean(false); //hasLockedRes pack
		startgame.writeBoolean(false); //hasLockedBeh pack
		startgame.writeBoolean(false); //hasLocked world template.
		StringSerializer.writeVarIntUTF8String(startgame, levelId);
		StringSerializer.writeVarIntUTF8String(startgame, ""); //level name (will packet.getLevelType() work?)
		StringSerializer.writeVarIntUTF8String(startgame, ""); //template pack id
		startgame.writeBoolean(false); //is trial
		startgame.writeLongLE(0); //level time
		VarNumberSerializer.writeSVarInt(startgame, 0); //enchantment seed
		packets.add(startgame);
		return packets;
	}

}
