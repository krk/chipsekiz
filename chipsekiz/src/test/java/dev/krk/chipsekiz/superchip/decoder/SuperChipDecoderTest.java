package dev.krk.chipsekiz.superchip.decoder;

import dev.krk.chipsekiz.DecoderTest;
import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.opcodes.OpDXYN;
import dev.krk.chipsekiz.superchip.opcodes.Op00CN;
import dev.krk.chipsekiz.superchip.opcodes.OpDXY0;
import org.junit.jupiter.api.Test;

public class SuperChipDecoderTest extends DecoderTest {
    @Override public IDecoder createDecoder() {
        return new SuperChipDecoder();
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
}
