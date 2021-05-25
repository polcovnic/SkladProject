package org.example;

import lombok.Data;
import com.google.common.primitives.UnsignedLong;
import com.github.snksoft.crc.CRC;

import java.nio.ByteBuffer;

@Data
public class Packet {
    private final static Byte bMagic = 0x13;

    private Byte bSrc;
    private UnsignedLong bPktId;
    private Integer wLen;
    private Message bMsq;

    private Short wCrc16_1;
    private Short wCrc16_2;

    public Packet(Byte bSrc, UnsignedLong bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsq = bMsq;

        wLen = bMsq.getMessageBytes();
    }

    public Packet(byte[] encodedPacket) throws Exception{
        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);

        Byte expectedBMagic = buffer.get();
        if (!expectedBMagic.equals(bMagic)) {
            throw new Exception("Unexpected bMagic");
        }

        bSrc = buffer.get();
        bPktId = UnsignedLong.fromLongBits(buffer.getLong());
        wLen = buffer.getInt();

        wCrc16_1 = buffer.getShort();
        byte[] packetFirstPart = getPacketFirstPart();
        Short expectedWCrc16_1 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetFirstPart);
        if (!expectedWCrc16_1.equals(wCrc16_1)) {
            throw new Exception("Unexpected CRC16");
        }

        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());
        byte[] messageBody = new byte[wLen];
        buffer.get(messageBody);
        bMsq.setMessage(new String(messageBody));

        wCrc16_2 = buffer.getShort();
        byte[] packetSecondPart = getPacketSecondPart();
        Short expectedWCrc16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetSecondPart);
        if (!expectedWCrc16_2.equals(wCrc16_2)) {
            throw new Exception("Unexpected CRC16");
        }

        bMsq.decode();
    }

    public byte[] toPacket() {
        Message message = getBMsq();
        message.encode();
        wLen = message.getMessageBytes();

        byte[] packetFirstPart = getPacketFirstPart();

        wCrc16_1 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetFirstPart);

        byte[] packetSecondPart = getPacketSecondPart();

        wCrc16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetSecondPart);

        int packetLength = getPacketFirstPartLength() + wCrc16_1.BYTES + getPacketSecondPartLength() + wCrc16_2.BYTES;

        return ByteBuffer.allocate(packetLength)
                .put(packetFirstPart)
                .putShort(wCrc16_1)
                .put(packetSecondPart)
                .putShort(wCrc16_2)
                .array();
    }

    private int getPacketFirstPartLength() {
        return bMagic.BYTES + bSrc.BYTES + Long.BYTES + wLen.BYTES;
    }

    private int getPacketSecondPartLength() {
        return bMsq.getMessageBytesLength();
    }

    private byte[] getPacketFirstPart() {
        return ByteBuffer.allocate(getPacketFirstPartLength())
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen)
                .array();
    }

    private byte[] getPacketSecondPart() {
        return ByteBuffer.allocate(getPacketSecondPartLength())
                .put(bMsq.packMessage())
                .array();
    }

}
