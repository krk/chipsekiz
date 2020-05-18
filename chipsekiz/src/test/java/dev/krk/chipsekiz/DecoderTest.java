package dev.krk.chipsekiz;

import dev.krk.chipsekiz.opcodes.Op00E0;
import dev.krk.chipsekiz.opcodes.Op00EE;
import dev.krk.chipsekiz.opcodes.Op0NNN;
import dev.krk.chipsekiz.opcodes.Op1NNN;
import dev.krk.chipsekiz.opcodes.Op2NNN;
import dev.krk.chipsekiz.opcodes.Op3XNN;
import dev.krk.chipsekiz.opcodes.Op4XNN;
import dev.krk.chipsekiz.opcodes.Op5XY0;
import dev.krk.chipsekiz.opcodes.Op6XNN;
import dev.krk.chipsekiz.opcodes.Op7XNN;
import dev.krk.chipsekiz.opcodes.Op8XY0;
import dev.krk.chipsekiz.opcodes.Op8XY1;
import dev.krk.chipsekiz.opcodes.Op8XY2;
import dev.krk.chipsekiz.opcodes.Op8XY3;
import dev.krk.chipsekiz.opcodes.Op8XY4;
import dev.krk.chipsekiz.opcodes.Op8XY5;
import dev.krk.chipsekiz.opcodes.Op8XY6;
import dev.krk.chipsekiz.opcodes.Op8XY7;
import dev.krk.chipsekiz.opcodes.Op8XYE;
import dev.krk.chipsekiz.opcodes.Op9XY0;
import dev.krk.chipsekiz.opcodes.OpANNN;
import dev.krk.chipsekiz.opcodes.OpBNNN;
import dev.krk.chipsekiz.opcodes.OpCXNN;
import dev.krk.chipsekiz.opcodes.OpDXYN;
import dev.krk.chipsekiz.opcodes.OpEX9E;
import dev.krk.chipsekiz.opcodes.OpEXA1;
import dev.krk.chipsekiz.opcodes.OpFX07;
import dev.krk.chipsekiz.opcodes.OpFX0A;
import dev.krk.chipsekiz.opcodes.OpFX15;
import dev.krk.chipsekiz.opcodes.OpFX18;
import dev.krk.chipsekiz.opcodes.OpFX1E;
import dev.krk.chipsekiz.opcodes.OpFX29;
import dev.krk.chipsekiz.opcodes.OpFX33;
import dev.krk.chipsekiz.opcodes.OpFX55;
import dev.krk.chipsekiz.opcodes.OpFX65;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.opcodes.OpcodeOrData;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.base.Strings.padStart;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecoderTest {

    @Test public void testDecodeInvalid() {
        Decoder decoder = new Decoder();

        assertIsData(decoder.decode((short) 0x5001), "5001");
        assertIsData(decoder.decode((short) 0x8008), "8008");
        assertIsData(decoder.decode((short) 0x8009), "8009");
        assertIsData(decoder.decode((short) 0x800A), "800A");
        assertIsData(decoder.decode((short) 0x800B), "800B");
        assertIsData(decoder.decode((short) 0x800C), "800C");
        assertIsData(decoder.decode((short) 0x800D), "800D");
        assertIsData(decoder.decode((short) 0x800F), "800F");
        assertIsData(decoder.decode((short) 0x9001), "9001");
        assertIsData(decoder.decode((short) 0xE000), "E000");
        assertIsData(decoder.decode((short) 0xF000), "F000");
    }

    @Test public void testDecode00E0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00E0), Op00E0.class, "00E0");
    }

    @Test public void testDecode00EE() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x00EE), Op00EE.class, "00EE");
    }

    @Test public void testDecode0NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x0000), Op0NNN.class, 0, "0000");
        assertOpcodeValid(decoder.decode((short) 0x0958), Op0NNN.class, 0x958, "0958");
        assertOpcodeValid(decoder.decode((short) 0x0FFF), Op0NNN.class, 0xFFF, "0FFF");
    }

    @Test public void testDecode1NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x1000), Op1NNN.class, 0, "1000");
        assertOpcodeValid(decoder.decode((short) 0x1424), Op1NNN.class, 0x424, "1424");
        assertOpcodeValid(decoder.decode((short) 0x1FFF), Op1NNN.class, 0xFFF, "1FFF");
    }

    @Test public void testDecode2NNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x2000), Op2NNN.class, 0, "2000");
        assertOpcodeValid(decoder.decode((short) 0x2424), Op2NNN.class, 0x424, "2424");
        assertOpcodeValid(decoder.decode((short) 0x2FFF), Op2NNN.class, 0xFFF, "2FFF");
    }

    @Test public void testDecode3XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x3000), Op3XNN.class, 0, 0, "3000");
        assertOpcodeValid(decoder.decode((short) 0x34A5), Op3XNN.class, 4, 0xA5, "34A5");
        assertOpcodeValid(decoder.decode((short) 0x3FFF), Op3XNN.class, 0xF, 0xFF, "3FFF");
    }

    @Test public void testDecode4XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x4000), Op4XNN.class, 0, 0, "4000");
        assertOpcodeValid(decoder.decode((short) 0x44A5), Op4XNN.class, 4, 0xA5, "44A5");
        assertOpcodeValid(decoder.decode((short) 0x4FFF), Op4XNN.class, 0xF, 0xFF, "4FFF");
    }

    @Test public void testDecode5XY0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x5000), Op5XY0.class, 0, 0, Optional.empty(),
            "5000");
        assertOpcodeValid(decoder.decode((short) 0x54A0), Op5XY0.class, 4, 0xA, Optional.empty(),
            "54A0");
        assertOpcodeValid(decoder.decode((short) 0x5FF0), Op5XY0.class, 0xF, 0xF, Optional.empty(),
            "5FF0");
    }

    @Test public void testDecode6XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x6000), Op6XNN.class, 0, 0, "6000");
        assertOpcodeValid(decoder.decode((short) 0x64A5), Op6XNN.class, 4, 0xA5, "64A5");
        assertOpcodeValid(decoder.decode((short) 0x6FFF), Op6XNN.class, 0xF, 0xFF, "6FFF");
    }

    @Test public void testDecode7XNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x7000), Op7XNN.class, 0, 0, "7000");
        assertOpcodeValid(decoder.decode((short) 0x74A5), Op7XNN.class, 4, 0xA5, "74A5");
        assertOpcodeValid(decoder.decode((short) 0x7FFF), Op7XNN.class, 0xF, 0xFF, "7FFF");
    }

    @Test public void testDecode8XY0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8000), Op8XY0.class, 0, 0, Optional.empty(),
            "8000");
        assertOpcodeValid(decoder.decode((short) 0x84A0), Op8XY0.class, 4, 0xA, Optional.empty(),
            "84A0");
        assertOpcodeValid(decoder.decode((short) 0x8FF0), Op8XY0.class, 0xF, 0xF, Optional.empty(),
            "8FF0");
    }

    @Test public void testDecode8XY1() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8001), Op8XY1.class, 0, 0, Optional.empty(),
            "8001");
        assertOpcodeValid(decoder.decode((short) 0x84A1), Op8XY1.class, 4, 0xA, Optional.empty(),
            "84A1");
        assertOpcodeValid(decoder.decode((short) 0x8FF1), Op8XY1.class, 0xF, 0xF, Optional.empty(),
            "8FF1");
    }

    @Test public void testDecode8XY2() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8002), Op8XY2.class, 0, 0, Optional.empty(),
            "8002");
        assertOpcodeValid(decoder.decode((short) 0x84A2), Op8XY2.class, 4, 0xA, Optional.empty(),
            "84A2");
        assertOpcodeValid(decoder.decode((short) 0x8FF2), Op8XY2.class, 0xF, 0xF, Optional.empty(),
            "8FF2");
    }

    @Test public void testDecode8XY3() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8003), Op8XY3.class, 0, 0, Optional.empty(),
            "8003");
        assertOpcodeValid(decoder.decode((short) 0x84A3), Op8XY3.class, 4, 0xA, Optional.empty(),
            "84A3");
        assertOpcodeValid(decoder.decode((short) 0x8FF3), Op8XY3.class, 0xF, 0xF, Optional.empty(),
            "8FF3");
    }

    @Test public void testDecode8XY4() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8004), Op8XY4.class, 0, 0, Optional.empty(),
            "8004");
        assertOpcodeValid(decoder.decode((short) 0x84A4), Op8XY4.class, 4, 0xA, Optional.empty(),
            "84A4");
        assertOpcodeValid(decoder.decode((short) 0x8FF4), Op8XY4.class, 0xF, 0xF, Optional.empty(),
            "8FF4");
    }

    @Test public void testDecode8XY5() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8005), Op8XY5.class, 0, 0, Optional.empty(),
            "8005");
        assertOpcodeValid(decoder.decode((short) 0x84A5), Op8XY5.class, 4, 0xA, Optional.empty(),
            "84A5");
        assertOpcodeValid(decoder.decode((short) 0x8FF5), Op8XY5.class, 0xF, 0xF, Optional.empty(),
            "8FF5");
    }

    @Test public void testDecode8XY6() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8006), Op8XY6.class, 0, 0, Optional.empty(),
            "8006");
        assertOpcodeValid(decoder.decode((short) 0x84A6), Op8XY6.class, 4, 0xA, Optional.empty(),
            "84A6");
        assertOpcodeValid(decoder.decode((short) 0x8FF6), Op8XY6.class, 0xF, 0xF, Optional.empty(),
            "8FF6");
    }

    @Test public void testDecode8XY7() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x8007), Op8XY7.class, 0, 0, Optional.empty(),
            "8007");
        assertOpcodeValid(decoder.decode((short) 0x84A7), Op8XY7.class, 4, 0xA, Optional.empty(),
            "84A7");
        assertOpcodeValid(decoder.decode((short) 0x8FF7), Op8XY7.class, 0xF, 0xF, Optional.empty(),
            "8FF7");
    }

    @Test public void testDecode8XYE() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x800E), Op8XYE.class, 0, 0, Optional.empty(),
            "800E");
        assertOpcodeValid(decoder.decode((short) 0x84AE), Op8XYE.class, 4, 0xA, Optional.empty(),
            "84AE");
        assertOpcodeValid(decoder.decode((short) 0x8FFE), Op8XYE.class, 0xF, 0xF, Optional.empty(),
            "8FFE");
    }

    @Test public void testDecode9XY0() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0x9000), Op9XY0.class, 0, 0, Optional.empty(),
            "9000");
        assertOpcodeValid(decoder.decode((short) 0x94A0), Op9XY0.class, 4, 0xA, Optional.empty(),
            "94A0");
        assertOpcodeValid(decoder.decode((short) 0x9FF0), Op9XY0.class, 0xF, 0xF, Optional.empty(),
            "9FF0");
    }

    @Test public void testDecodeANNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0xA000), OpANNN.class, 0, "A000");
        assertOpcodeValid(decoder.decode((short) 0xA958), OpANNN.class, 0x958, "A958");
        assertOpcodeValid(decoder.decode((short) 0xAFFF), OpANNN.class, 0xFFF, "AFFF");
    }

    @Test public void testDecodeBNNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0xB000), OpBNNN.class, 0, "B000");
        assertOpcodeValid(decoder.decode((short) 0xB958), OpBNNN.class, 0x958, "B958");
        assertOpcodeValid(decoder.decode((short) 0xBFFF), OpBNNN.class, 0xFFF, "BFFF");
    }

    @Test public void testDecodeCXNN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0xC000), OpCXNN.class, 0, 0, "C000");
        assertOpcodeValid(decoder.decode((short) 0xC4A5), OpCXNN.class, 4, 0xA5, "C4A5");
        assertOpcodeValid(decoder.decode((short) 0xCFFF), OpCXNN.class, 0xF, 0xFF, "CFFF");
    }

    @Test public void testDecodeDXYN() {
        Decoder decoder = new Decoder();

        assertOpcodeValid(decoder.decode((short) 0xD000), OpDXYN.class, 0, 0, Optional.of(0),
            "D000");
        assertOpcodeValid(decoder.decode((short) 0xD4A5), OpDXYN.class, 4, 0xA, Optional.of(5),
            "D4A5");
        assertOpcodeValid(decoder.decode((short) 0xDFFF), OpDXYN.class, 0xF, 0xF, Optional.of(0xF),
            "DFFF");
    }

    @Test public void testDecodeEX9E() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xE09E), OpEX9E.class, 0, "E09E");
        assertOpcodeValidVx(decoder.decode((short) 0xE49E), OpEX9E.class, 4, "E49E");
        assertOpcodeValidVx(decoder.decode((short) 0xEF9E), OpEX9E.class, 0xF, "EF9E");
    }

    @Test public void testDecodeEXA1() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xE0A1), OpEXA1.class, 0, "E0A1");
        assertOpcodeValidVx(decoder.decode((short) 0xE4A1), OpEXA1.class, 4, "E4A1");
        assertOpcodeValidVx(decoder.decode((short) 0xEFA1), OpEXA1.class, 0xF, "EFA1");
    }

    @Test public void testDecodeFX07() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF007), OpFX07.class, 0, "F007");
        assertOpcodeValidVx(decoder.decode((short) 0xF407), OpFX07.class, 4, "F407");
        assertOpcodeValidVx(decoder.decode((short) 0xFF07), OpFX07.class, 0xF, "FF07");
    }

    @Test public void testDecodeFX0A() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF00A), OpFX0A.class, 0, "F00A");
        assertOpcodeValidVx(decoder.decode((short) 0xF40A), OpFX0A.class, 4, "F40A");
        assertOpcodeValidVx(decoder.decode((short) 0xFF0A), OpFX0A.class, 0xF, "FF0A");
    }

    @Test public void testDecodeFX15() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF015), OpFX15.class, 0, "F015");
        assertOpcodeValidVx(decoder.decode((short) 0xF415), OpFX15.class, 4, "F415");
        assertOpcodeValidVx(decoder.decode((short) 0xFF15), OpFX15.class, 0xF, "FF15");
    }

    @Test public void testDecodeFX18() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF018), OpFX18.class, 0, "F018");
        assertOpcodeValidVx(decoder.decode((short) 0xF418), OpFX18.class, 4, "F418");
        assertOpcodeValidVx(decoder.decode((short) 0xFF18), OpFX18.class, 0xF, "FF18");
    }

    @Test public void testDecodeFX1E() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF01E), OpFX1E.class, 0, "F01E");
        assertOpcodeValidVx(decoder.decode((short) 0xF41E), OpFX1E.class, 4, "F41E");
        assertOpcodeValidVx(decoder.decode((short) 0xFF1E), OpFX1E.class, 0xF, "FF1E");
    }

    @Test public void testDecodeFX29() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF029), OpFX29.class, 0, "F029");
        assertOpcodeValidVx(decoder.decode((short) 0xF429), OpFX29.class, 4, "F429");
        assertOpcodeValidVx(decoder.decode((short) 0xFF29), OpFX29.class, 0xF, "FF29");
    }

    @Test public void testDecodeFX33() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF033), OpFX33.class, 0, "F033");
        assertOpcodeValidVx(decoder.decode((short) 0xF433), OpFX33.class, 4, "F433");
        assertOpcodeValidVx(decoder.decode((short) 0xFF33), OpFX33.class, 0xF, "FF33");
    }

    @Test public void testDecodeFX55() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF055), OpFX55.class, 0, "F055");
        assertOpcodeValidVx(decoder.decode((short) 0xF455), OpFX55.class, 4, "F455");
        assertOpcodeValidVx(decoder.decode((short) 0xFF55), OpFX55.class, 0xF, "FF55");
    }

    @Test public void testDecodeFX65() {
        Decoder decoder = new Decoder();

        assertOpcodeValidVx(decoder.decode((short) 0xF065), OpFX65.class, 0, "F065");
        assertOpcodeValidVx(decoder.decode((short) 0xF465), OpFX65.class, 4, "F465");
        assertOpcodeValidVx(decoder.decode((short) 0xFF65), OpFX65.class, 0xF, "FF65");
    }

    private static void assertOpcodeValid(OpcodeOrData od, Class type, String s) {
        assertIsOpcode(od);
        Opcode opcode = od.opcode();
        assertTrue(type.isInstance(opcode));

        assertTrue(opcode.getAddress().isEmpty());

        assertTrue(opcode.getVx().isEmpty());
        assertTrue(opcode.getVy().isEmpty());

        assertEquals(s, opcode.encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(opcode.getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertOpcodeValid(OpcodeOrData od, Class type, int address, String s) {
        assertIsOpcode(od);
        Opcode opcode = od.opcode();
        assertTrue(type.isInstance(opcode));

        assertTrue(opcode.getAddress().isPresent());
        assertEquals(opcode.getAddress(), Optional.of(address));

        assertTrue(opcode.getVx().isEmpty());
        assertTrue(opcode.getVy().isEmpty());

        assertEquals(s, opcode.encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(opcode.getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertOpcodeValidVx(OpcodeOrData od, Class type, int vx, String s) {
        assertIsOpcode(od);
        Opcode opcode = od.opcode();
        assertTrue(type.isInstance(opcode));

        assertTrue(opcode.getAddress().isEmpty());

        assertTrue(opcode.getVx().isPresent());
        assertEquals((int) opcode.getVx().get(), vx);

        assertTrue(opcode.getVy().isEmpty());

        assertEquals(s, opcode.encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(opcode.getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertOpcodeValid(OpcodeOrData od, Class type, int vx, int address,
        String s) {
        assertIsOpcode(od);
        Opcode opcode = od.opcode();
        assertTrue(type.isInstance(opcode));

        assertTrue(opcode.getAddress().isPresent());
        assertEquals(opcode.getAddress(), Optional.of(address));

        assertTrue(opcode.getVx().isPresent());
        assertEquals((int) opcode.getVx().get(), vx);

        assertTrue(opcode.getVy().isEmpty());

        assertEquals(s, opcode.encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(opcode.getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertOpcodeValid(OpcodeOrData od, Class type, int vx, int vy,
        Optional<Integer> address, String s) {
        assertIsOpcode(od);
        Opcode opcode = od.opcode();
        assertTrue(type.isInstance(opcode));

        assertEquals(opcode.getAddress(), address);

        assertTrue(opcode.getVx().isPresent());
        assertEquals((int) opcode.getVx().get(), vx);

        assertTrue(opcode.getVy().isPresent());
        assertEquals((int) opcode.getVy().get(), vy);

        assertEquals(s, opcode.encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(opcode.getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertIsData(OpcodeOrData od, String s) {
        assertSame(od.getKind(), OpcodeOrData.Kind.DATA);

        assertEquals(s, od.data().encode());
        assertEquals(s, od.encode());
        assertEquals(s, padStart(shortToHex(od.data().getValue()).toUpperCase(), 4, '0'));
        assertEquals(s, padStart(shortToHex(od.getValue()).toUpperCase(), 4, '0'));
    }

    private static void assertIsOpcode(OpcodeOrData od) {
        assertSame(od.getKind(), OpcodeOrData.Kind.OPCODE);
    }

    private static String shortToHex(short s) {
        return String.format("%02X%02X", (byte) ((s & 0xFF00) >> 8), (byte) (s & 0xFF));
    }
}
