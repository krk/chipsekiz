package dev.krk.emulator;

import junit.framework.TestCase;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class DecoderTest extends TestCase {

    public void testDecode00E0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00E0), OpcodeType.Op00E0);
    }

    public void testDecode00EE() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00EE), OpcodeType.Op00EE);
    }

    public void testDecode0NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x0000), OpcodeType.Op0NNN, 0);
        assertOpcodeValid(decoder.decode((short) 0x0958), OpcodeType.Op0NNN, 0x958);
        assertOpcodeValid(decoder.decode((short) 0x0FFF), OpcodeType.Op0NNN, 0xFFF);
    }

    public void testDecode1NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x1000), OpcodeType.Op1NNN, 0);
        assertOpcodeValid(decoder.decode((short) 0x1424), OpcodeType.Op1NNN, 0x424);
        assertOpcodeValid(decoder.decode((short) 0x1FFF), OpcodeType.Op1NNN, 0xFFF);
    }

    public void testDecode2NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x2000), OpcodeType.Op2NNN, 0);
        assertOpcodeValid(decoder.decode((short) 0x2424), OpcodeType.Op2NNN, 0x424);
        assertOpcodeValid(decoder.decode((short) 0x2FFF), OpcodeType.Op2NNN, 0xFFF);
    }

    public void testDecode3XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x3000), OpcodeType.Op3XNN, 0, 0);
        assertOpcodeValid(decoder.decode((short) 0x34A5), OpcodeType.Op3XNN, 4, 0xA5);
        assertOpcodeValid(decoder.decode((short) 0x3FFF), OpcodeType.Op3XNN, 0xF, 0xFF);
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, OpcodeType type) {
        assertTrue(opcode.isPresent());
        assertEquals(opcode.get().getType(), type);

        assertTrue(opcode.get().getAddress().isEmpty());

        assertTrue(opcode.get().getVx().isEmpty());
        assertTrue(opcode.get().getVy().isEmpty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, OpcodeType type, int address) {
        assertTrue(opcode.isPresent());
        assertEquals(opcode.get().getType(), type);

        assertTrue(opcode.get().getAddress().isPresent());
        assertEquals(opcode.get().getAddress(), Optional.of(address));

        assertTrue(opcode.get().getVx().isEmpty());
        assertTrue(opcode.get().getVy().isEmpty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, OpcodeType type, int vx,
        int address) {
        assertTrue(opcode.isPresent());
        assertEquals(opcode.get().getType(), type);

        assertTrue(opcode.get().getAddress().isPresent());
        assertEquals(opcode.get().getAddress(), Optional.of(address));

        assertTrue(opcode.get().getVx().isPresent());
        assertEquals(opcode.get().getVx().get().getIndex(), vx);

        assertTrue(opcode.get().getVy().isEmpty());
    }
}

