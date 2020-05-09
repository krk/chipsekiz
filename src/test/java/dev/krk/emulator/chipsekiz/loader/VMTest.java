package dev.krk.emulator.chipsekiz.loader;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VMTest {

    @Test void constructor() {
        VM vm = new VM(0x200, 0x1000);

        assertEquals(0x200, vm.getOrigin());
        assertEquals(0x1000, vm.getMemorySize());
    }

    @Test void loadDefaultVM() {
        VM vm = new VM();
        Layout layout = new Layout(Arrays.asList(
            new Section[] {new Section(0, new byte[] {(byte) 0xE0}),
                new Section(0x300, new byte[] {(byte) 1, 2, 3, 4})}));

        vm.load(new byte[] {(byte) 0xC0}, layout);

        assertEquals((byte) 0xC0, vm.getByte(0x200));

        assertEquals((byte) 0xE0, vm.getByte(0));
        assertEquals((short) 0xE000, vm.getShort(0));
        assertEquals(0xE0000000, vm.getInt(0));

        assertEquals((byte) 0x01, vm.getByte(0x300));
        assertEquals((short) 0x0102, vm.getShort(0x300));
        assertEquals(0x01020304, vm.getInt(0x300));
    }

    @Test void load() {
        VM vm = new VM(0x100, 0x304);
        Layout layout = new Layout(Arrays.asList(
            new Section[] {new Section(0, new byte[] {(byte) 0xE0}),
                new Section(0x300, new byte[] {(byte) 1, 2, 3, 4})}));

        vm.load(new byte[] {(byte) 0xC0}, layout);

        assertEquals((byte) 0xC0, vm.getByte(0x100));

        assertEquals((byte) 0xE0, vm.getByte(0));
        assertEquals((short) 0xE000, vm.getShort(0));
        assertEquals(0xE0000000, vm.getInt(0));

        assertEquals((byte) 0x01, vm.getByte(0x300));
        assertEquals((short) 0x0102, vm.getShort(0x300));
        assertEquals(0x01020304, vm.getInt(0x300));
    }

    @Test void registers() {
        VM vm = new VM();

        vm.setRegister(0, (byte) 2);
        assertEquals(2, vm.getRegister(0));
        vm.setRegister(0xE, (byte) 9);
        assertEquals(9, vm.getRegister(0xE));

        assertThrows(IllegalArgumentException.class, () -> vm.getRegister(16));

        assertEquals(0, vm.getRegister(0xF));
        vm.setCarryFlag();
        assertEquals(true, vm.getCarryFlag());
        assertEquals(1, vm.getRegister(0xF));
        vm.resetCarryFlag();
        assertEquals(0, vm.getRegister(0xF));
    }

    @Test void stack() {
        VM vm = new VM();

        assertThrows(IllegalStateException.class, () -> vm.pop());

        vm.push((short) 0x4142);
        assertEquals(0x4142, vm.pop());

        assertThrows(IllegalStateException.class, () -> vm.pop());

        for (short i = 0; i < 16; i++) {
            vm.push(i);
        }

        assertThrows(IllegalStateException.class, () -> vm.push((short) 0xF435));
    }
}
