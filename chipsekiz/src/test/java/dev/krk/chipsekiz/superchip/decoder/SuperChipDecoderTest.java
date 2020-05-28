package dev.krk.chipsekiz.superchip.decoder;

import dev.krk.chipsekiz.DecoderTest;
import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.opcodes.OpDXYN;
import dev.krk.chipsekiz.superchip.opcodes.Op00CN;
import dev.krk.chipsekiz.superchip.opcodes.Op00FB;
import dev.krk.chipsekiz.superchip.opcodes.Op00FC;
import dev.krk.chipsekiz.superchip.opcodes.Op00FD;
import dev.krk.chipsekiz.superchip.opcodes.Op00FE;
import dev.krk.chipsekiz.superchip.opcodes.Op00FF;
import dev.krk.chipsekiz.superchip.opcodes.OpDXY0;
import dev.krk.chipsekiz.superchip.opcodes.OpFX30;
import dev.krk.chipsekiz.superchip.opcodes.OpFX75;
import dev.krk.chipsekiz.superchip.opcodes.OpFX85;
import org.junit.jupiter.api.Test;

public class SuperChipDecoderTest extends DecoderTest {
    @Override public IDecoder createDecoder() {
        return new SuperChipDecoder();
    }

    @Test public void testDecode00FB() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00FB), Op00FB.class, "00FB");
    }

    @Test public void testDecode00FC() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00FC), Op00FC.class, "00FC");
    }

    @Test public void testDecode00FD() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00FD), Op00FD.class, "00FD");
    }

    @Test public void testDecode00FE() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00FE), Op00FE.class, "00FE");
    }

    @Test public void testDecode00FF() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00FF), Op00FF.class, "00FF");
    }

    @Test public void testDecode00CN() {
        IDecoder decoder = new SuperChipDecoder();

        assertOpcodeValid(decoder.decode((short) 0x00C9), Op00CN.class, 9, "00C9");
    }

    @Test public void testDecodeDXY0() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0xD000), OpDXY0.class, 0, 0, 0, "D000");
        assertOpcodeValid(decoder.decode((short) 0xD100), OpDXY0.class, 1, 0, 0, "D100");
        assertOpcodeValid(decoder.decode((short) 0xD010), OpDXY0.class, 0, 1, 0, "D010");
    }

    @Test @Override public void testDecodeDXYN() {
        IDecoder decoder = createDecoder();

        assertOpcodeValid(decoder.decode((short) 0xD001), OpDXYN.class, 0, 0, 1, "D001");
        assertOpcodeValid(decoder.decode((short) 0xD4A5), OpDXYN.class, 4, 0xA, 5, "D4A5");
        assertOpcodeValid(decoder.decode((short) 0xDFFF), OpDXYN.class, 0xF, 0xF, 0xF, "DFFF");
    }

    @Test public void testDecodeFX30() {
        IDecoder decoder = createDecoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF030), OpFX30.class, 0, "F030");
        assertOpcodeValidVx(decoder.decode((short) 0xF430), OpFX30.class, 4, "F430");
        assertOpcodeValidVx(decoder.decode((short) 0xFF30), OpFX30.class, 0xF, "FF30");
    }

    @Test public void testDecodeFX75() {
        IDecoder decoder = createDecoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF075), OpFX75.class, 0, "F075");
        assertOpcodeValidVx(decoder.decode((short) 0xF475), OpFX75.class, 4, "F475");
        assertOpcodeValidVx(decoder.decode((short) 0xFF75), OpFX75.class, 0xF, "FF75");
    }

    @Test public void testDecodeFX85() {
        IDecoder decoder = createDecoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF085), OpFX85.class, 0, "F085");
        assertOpcodeValidVx(decoder.decode((short) 0xF485), OpFX85.class, 4, "F485");
        assertOpcodeValidVx(decoder.decode((short) 0xFF85), OpFX85.class, 0xF, "FF85");
    }
}
