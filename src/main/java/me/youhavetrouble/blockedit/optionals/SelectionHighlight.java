package me.youhavetrouble.blockedit.optionals;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.youhavetrouble.blockedit.BlockEdit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * Highlighting selection blocks thanks to https://github.com/ArtFect/BlockHighlight
 *
 * MIT License
 *
 * Copyright (c) 2021 ArtFect
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class SelectionHighlight {

    public static void highlightBlock(Player player, Location location, String color, String text, int time) {
        if (BlockEdit.getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            sendBlockHighlight(player, location, color, text, time);
        }
    }

    public static void sendStop(Player pl) {
        sendPayload(pl, "debug/game_test_clear", Unpooled.wrappedBuffer(new byte[0]));
    }

    private static void sendBlockHighlight(Player player, Location location, String hex, String text, int time) {
        ByteBuf packet = Unpooled.buffer();
        packet.writeLong(blockPosToLong((int)location.getX(), (int)location.getY(), (int)location.getZ()));
        int color = hex2Rgb(hex, 100).getRGB();
        packet.writeInt(color);
        writeString(packet, text);
        packet.writeInt(time);
        sendPayload(player, "debug/game_test_add_marker", packet);
    }

    private static long blockPosToLong(int x, int y, int z) {
        return ((long) x & 67108863L) << 38 | (long) y & 4095L | ((long) z & 67108863L) << 12;
    }

    private static void writeBytes(ByteBuf packet, int i) {
        while ((i & -128) != 0) {
            packet.writeByte(i & 127 | 128);
            i >>>= 7;
        }
        packet.writeByte(i);
    }

    private static void writeString(ByteBuf packet, String s) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);
        writeBytes(packet, abyte.length);
        packet.writeBytes(abyte);
    }

    private static void sendPayload(Player receiver, String channel, ByteBuf bytes) {
        PacketContainer handle = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
        handle.getMinecraftKeys().write(0, new MinecraftKey(channel));

        Object serializer = MinecraftReflection.getPacketDataSerializer(bytes);
        handle.getModifier().withType(ByteBuf.class).write(0, serializer);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, handle);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to send the packet", e);
        }
    }

    //https://stackoverflow.com/questions/4129666/how-to-convert-hex-to-rgb-using-java/4129692
    private static Color hex2Rgb(String colorStr, int transparency) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16), transparency);
    }

}
