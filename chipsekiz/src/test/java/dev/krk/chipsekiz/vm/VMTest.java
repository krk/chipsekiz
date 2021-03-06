package dev.krk.chipsekiz.vm;

import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.loader.Loader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VMTest {

    @Test void constructor() {
        VM vm = new VM(0x200, new byte[0x1000]);

        assertEquals(0x200, vm.getOrigin());
        assertEquals(0x1000, vm.getMemorySize());
    }

    @Test void registers() {
        VM vm = new VM();

        vm.setRegister(0, (byte) 2);
        assertEquals(2, vm.getRegister(0));
        vm.setRegister(0xE, (byte) 9);
        assertEquals(9, vm.getRegister(0xE));

        assertThrows(IllegalArgumentException.class, () -> vm.getRegister(16));

        assertEquals(0, vm.getRegister(0xF));
        vm.setCarry();
        assertTrue(vm.hasCarry());
        assertEquals(1, vm.getRegister(0xF));
        vm.setCarry(false);
        assertEquals(0, vm.getRegister(0xF));

        assertEquals(0, vm.getI());
        vm.setI((short) 0xF035);
        assertEquals((short) 0xF035, vm.getI());

        assertEquals(vm.getOrigin(), vm.getPC());
        vm.setPC(0);
        assertEquals(0, vm.getPC());
        assertThrows(IllegalArgumentException.class, () -> vm.setPC(-1));
        assertDoesNotThrow(() -> vm.setPC(vm.getMemorySize()));
        assertDoesNotThrow(() -> vm.setPC(vm.getMemorySize() + 1));
        assertDoesNotThrow(() -> vm.setPC(vm.getMemorySize() + 2));
        assertThrows(IllegalArgumentException.class, () -> vm.setPC(vm.getMemorySize() + 3));
    }

    @Test void stack() {
        VM vm = new VM();

        assertThrows(IllegalStateException.class, vm::pop);

        vm.push((short) 0x4142);
        assertEquals(0x4142, vm.pop());

        assertThrows(IllegalStateException.class, vm::pop);

        for (short i = 0; i < 16; i++) {
            vm.push(i);
        }

        assertThrows(IllegalStateException.class, () -> vm.push((short) 0xF435));
    }

    @Test void timers() {
        VM vm = new VM();

        assertFalse(vm.hasDelay());
        assertFalse(vm.hasSound());

        vm.setDelayTimer((byte) 2);
        assertTrue(vm.hasDelay());
        vm.tickTimers();
        assertTrue(vm.hasDelay());
        vm.tickTimers();
        assertFalse(vm.hasSound());

        vm.setDelayTimer((byte) 1);
        vm.setSoundTimer((byte) 2);
        assertTrue(vm.hasDelay());
        assertTrue(vm.hasSound());

        vm.tickTimers();
        assertFalse(vm.hasDelay());
        assertTrue(vm.hasSound());

        vm.tickTimers();
        assertFalse(vm.hasDelay());
        assertFalse(vm.hasSound());

        vm.tickTimers();
        assertFalse(vm.hasDelay());
        assertFalse(vm.hasSound());

        vm.setDelayTimer((byte) 100);
        vm.setSoundTimer((byte) 0xFF);

        assertEquals(100, vm.getDelayTimer());
        assertEquals((byte) 0xFF, vm.getSoundTimer());
    }

    @Test void readMemory() {
        byte[] memory = new Loader().load(1, new byte[] {(byte) 0xC0}, 4, Layout.empty());
        VM vm = new VM(1, memory);

        assertEquals((byte) 0xC0, vm.getByte(1));

        assertEquals((byte) 0, vm.getByte(0));
        assertEquals((short) 0x00C0, vm.getShort(0));
        assertEquals(0x00C00000, vm.getInt(0));

        assertThrows(IllegalArgumentException.class, () -> vm.getByte(5));
        assertThrows(IllegalArgumentException.class, () -> vm.getShort(3));
        assertThrows(IllegalArgumentException.class, () -> vm.getInt(1));
    }
}
