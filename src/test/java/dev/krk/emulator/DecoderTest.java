package dev.krk.emulator;

import junit.framework.TestCase;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class DecoderTest extends TestCase {

    public void testDecode() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x1000), OpcodeType.Op1NNN, 0);
        assertOpcodeValid(decoder.decode((short) 0x1424), OpcodeType.Op1NNN, 0x424);
        assertOpcodeValid(decoder.decode((short) 0x1FFF), OpcodeType.Op1NNN, 0xFFF);
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, OpcodeType type, int address) {
        assertTrue(opcode.isPresent());
        assertEquals(opcode.get().getType(), type);

        assertTrue(opcode.get().getAddress().isPresent());
        assertEquals(opcode.get().getAddress(), Optional.of(address));

        assertTrue(opcode.get().getVx().isEmpty());
        assertTrue(opcode.get().getVy().isEmpty());
    }
}
