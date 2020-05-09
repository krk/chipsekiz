package dev.krk.emulator.chipsekiz.loader;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
