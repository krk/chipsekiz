package dev.krk.emulator.chipsekiz;

import dev.krk.emulator.chipsekiz.opcodes.Op00E0;
import dev.krk.emulator.chipsekiz.opcodes.Op00EE;
import dev.krk.emulator.chipsekiz.opcodes.Op0NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op2NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op3XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op4XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op5XY0;
import dev.krk.emulator.chipsekiz.opcodes.Op6XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op7XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY0;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import junit.framework.TestCase;

import java.util.Optional;

public class DecoderTest extends TestCase {

    public void testDecode00E0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00E0), Op00E0.class);
    }

    public void testDecode00EE() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00EE), Op00EE.class);
    }

    public void testDecode0NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x0000), Op0NNN.class, 0);
        assertOpcodeValid(decoder.decode((short) 0x0958), Op0NNN.class, 0x958);
        assertOpcodeValid(decoder.decode((short) 0x0FFF), Op0NNN.class, 0xFFF);
    }

    public void testDecode1NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x1000), Op1NNN.class, 0);
        assertOpcodeValid(decoder.decode((short) 0x1424), Op1NNN.class, 0x424);
        assertOpcodeValid(decoder.decode((short) 0x1FFF), Op1NNN.class, 0xFFF);
    }

    public void testDecode2NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x2000), Op2NNN.class, 0);
        assertOpcodeValid(decoder.decode((short) 0x2424), Op2NNN.class, 0x424);
        assertOpcodeValid(decoder.decode((short) 0x2FFF), Op2NNN.class, 0xFFF);
    }

    public void testDecode3XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x3000), Op3XNN.class, 0, 0);
        assertOpcodeValid(decoder.decode((short) 0x34A5), Op3XNN.class, 4, 0xA5);
        assertOpcodeValid(decoder.decode((short) 0x3FFF), Op3XNN.class, 0xF, 0xFF);
    }

    public void testDecode4XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x4000), Op4XNN.class, 0, 0);
        assertOpcodeValid(decoder.decode((short) 0x44A5), Op4XNN.class, 4, 0xA5);
        assertOpcodeValid(decoder.decode((short) 0x4FFF), Op4XNN.class, 0xF, 0xFF);
    }

    public void testDecode5XY0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x5000), Op5XY0.class, 0, 0, Optional.empty());
        assertOpcodeValid(decoder.decode((short) 0x54A0), Op5XY0.class, 4, 0xA, Optional.empty());
        assertOpcodeValid(decoder.decode((short) 0x5FF0), Op5XY0.class, 0xF, 0xF, Optional.empty());
    }

    public void testDecode6XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x6000), Op6XNN.class, 0, 0);
        assertOpcodeValid(decoder.decode((short) 0x64A5), Op6XNN.class, 4, 0xA5);
        assertOpcodeValid(decoder.decode((short) 0x6FFF), Op6XNN.class, 0xF, 0xFF);
    }

    public void testDecode7XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x7000), Op7XNN.class, 0, 0);
        assertOpcodeValid(decoder.decode((short) 0x74A5), Op7XNN.class, 4, 0xA5);
        assertOpcodeValid(decoder.decode((short) 0x7FFF), Op7XNN.class, 0xF, 0xFF);
    }

    public void testDecode8XY0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8000), Op8XY0.class, 0, 0, Optional.empty());
        assertOpcodeValid(decoder.decode((short) 0x84A0), Op8XY0.class, 4, 0xA, Optional.empty());
        assertOpcodeValid(decoder.decode((short) 0x8FF0), Op8XY0.class, 0xF, 0xF, Optional.empty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, Class type) {
        assertTrue(opcode.isPresent());
        assertTrue(type.isInstance(opcode.get()));

        assertTrue(opcode.get().getAddress().isEmpty());

        assertTrue(opcode.get().getVx().isEmpty());
        assertTrue(opcode.get().getVy().isEmpty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, Class type, int address) {
        assertTrue(opcode.isPresent());
        assertTrue(type.isInstance(opcode.get()));

        assertTrue(opcode.get().getAddress().isPresent());
        assertEquals(opcode.get().getAddress(), Optional.of(address));

        assertTrue(opcode.get().getVx().isEmpty());
        assertTrue(opcode.get().getVy().isEmpty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, Class type, int vx,
        int address) {
        assertTrue(opcode.isPresent());
        assertTrue(type.isInstance(opcode.get()));

        assertTrue(opcode.get().getAddress().isPresent());
        assertEquals(opcode.get().getAddress(), Optional.of(address));

        assertTrue(opcode.get().getVx().isPresent());
        assertTrue(opcode.get().getVx().get() == vx);

        assertTrue(opcode.get().getVy().isEmpty());
    }

    private static void assertOpcodeValid(Optional<Opcode> opcode, Class type, int vx, int vy,
        Optional<Integer> address) {
        assertTrue(opcode.isPresent());
        assertTrue(type.isInstance(opcode.get()));

        assertEquals(opcode.get().getAddress(), address);

        assertTrue(opcode.get().getVx().isPresent());
        assertTrue(opcode.get().getVx().get() == vx);

        assertTrue(opcode.get().getVy().isPresent());
        assertTrue(opcode.get().getVy().get() == vy);
    }
}
