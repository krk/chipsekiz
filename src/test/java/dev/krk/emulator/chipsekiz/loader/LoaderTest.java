package dev.krk.emulator.chipsekiz.loader;

import dev.krk.emulator.chipsekiz.vm.VM;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoaderTest {
    @Test void loadDefaultVM() {
        Layout layout = new Layout(Arrays.asList(
            new Section[] {new Section(0, new byte[] {(byte) 0xE0}),
                new Section(0x300, new byte[] {(byte) 1, 2, 3, 4})}));
        byte[] memory = new Loader().load(new byte[] {(byte) 0xC0}, layout);
        VM vm = new VM(Loader.DefaultOrigin, memory);

        assertEquals((byte) 0xC0, vm.getByte(0x200));

        assertEquals((byte) 0xE0, vm.getByte(0));
        assertEquals((short) 0xE000, vm.getShort(0));
        assertEquals(0xE0000000, vm.getInt(0));

        assertEquals((byte) 0x01, vm.getByte(0x300));
        assertEquals((short) 0x0102, vm.getShort(0x300));
        assertEquals(0x01020304, vm.getInt(0x300));
    }

    @Test void load() {
        Layout layout = new Layout(Arrays.asList(
            new Section[] {new Section(0, new byte[] {(byte) 0xE0}),
                new Section(0x300, new byte[] {(byte) 1, 2, 3, 4})}));

        Loader loader = new Loader();
        byte[] memory = loader.load(0x100, new byte[304], 0x1000, layout);
        memory[0x100] = (byte) 0xC0;
        VM vm = new VM(0x200, memory);

        assertEquals((byte) 0xC0, vm.getByte(0x100));

        assertEquals((byte) 0xE0, vm.getByte(0));
        assertEquals((short) 0xE000, vm.getShort(0));
        assertEquals(0xE0000000, vm.getInt(0));

        assertEquals((byte) 0x01, vm.getByte(0x300));
        assertEquals((short) 0x0102, vm.getShort(0x300));
        assertEquals(0x01020304, vm.getInt(0x300));
    }
}
