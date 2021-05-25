package org.example;

import static org.junit.Assert.assertTrue;

import com.google.common.primitives.UnsignedLong;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private static final Byte bSrc = new Integer(5).byteValue();
    private static final UnsignedLong bPktId = UnsignedLong.valueOf(5);
    private static final Integer cType = 5;
    private static final Integer bUserId = 5;
    private static final String messageBody = "hey dude";

    private static final byte[] packetBytes = new Packet(bSrc, bPktId, new Message(cType, bUserId, messageBody)).toPacket();
    private static Packet packet;

    static {
        try {
            packet = new Packet(packetBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppTest() throws Exception {
    }

    @Test
    public void testClientAppNumber() {
        Assert.assertEquals(bSrc, packet.getBSrc());
    }

    @Test
    public void testPackageNumber() {
        Assert.assertEquals(bPktId, packet.getBPktId());
    }

    @Test
    public void testCommandType() {
        Assert.assertEquals(cType, packet.getBMsq().getCType());
    }

    @Test
    public void testUserId() {
        Assert.assertEquals(bUserId, packet.getBMsq().getBUserId());
    }

    @Test
    public void testMessageBody() {
        Assert.assertEquals(messageBody, packet.getBMsq().getMessage());
    }
}
