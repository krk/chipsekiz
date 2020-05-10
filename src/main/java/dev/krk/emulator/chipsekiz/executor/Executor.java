package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
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
import dev.krk.emulator.chipsekiz.opcodes.Op8XY1;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY2;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY3;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY4;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY5;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY6;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY7;
import dev.krk.emulator.chipsekiz.opcodes.Op8XYE;
import dev.krk.emulator.chipsekiz.opcodes.Op9XY0;
import dev.krk.emulator.chipsekiz.opcodes.OpANNN;
import dev.krk.emulator.chipsekiz.opcodes.OpBNNN;
import dev.krk.emulator.chipsekiz.opcodes.OpCXNN;
import dev.krk.emulator.chipsekiz.opcodes.OpDXYN;
import dev.krk.emulator.chipsekiz.opcodes.OpEX9E;
import dev.krk.emulator.chipsekiz.opcodes.OpFX15;
import dev.krk.emulator.chipsekiz.opcodes.OpFX18;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.vm.VM;

import java.util.Optional;

public class Executor implements IExecutor {
    @Override public void execute(VM vm, IHal hal, Opcode opcode) {
        if (opcode instanceof Op00E0) {
            hal.clearScreen();
        } else if (opcode instanceof Op00EE) {
            vm.setPC(vm.pop());
        } else if (opcode instanceof Op0NNN o) {
            vm.push(vm.getPC());
            vm.setPC(o.address());
        } else if (opcode instanceof Op1NNN o) {
            vm.setPC(o.address());
        } else if (opcode instanceof Op2NNN o) {
            vm.push(vm.getPC());
            vm.setPC(o.address());
        } else if (opcode instanceof Op3XNN o) {
            if (vm.getRegister(o.vx()) == o.imm()) {
                vm.setPC(vm.getPC() + 2);
            }
        } else if (opcode instanceof Op4XNN o) {
            if (vm.getRegister(o.vx()) != o.imm()) {
                vm.setPC(vm.getPC() + 2);
            }
        } else if (opcode instanceof Op5XY0 o) {
            if (vm.getRegister(o.vx()) == vm.getRegister(o.vy())) {
                vm.setPC(vm.getPC() + 2);
            }
        } else if (opcode instanceof Op6XNN o) {
            vm.setRegister(o.vx(), o.imm());
        } else if (opcode instanceof Op7XNN o) {
            vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) + o.imm()));
        } else if (opcode instanceof Op8XY0 o) {
            vm.setRegister(o.vx(), vm.getRegister(o.vy()));
        } else if (opcode instanceof Op8XY1 o) {
            vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) | vm.getRegister(o.vy())));
        } else if (opcode instanceof Op8XY2 o) {
            vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) & vm.getRegister(o.vy())));
        } else if (opcode instanceof Op8XY3 o) {
            vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) ^ vm.getRegister(o.vy())));
        } else if (opcode instanceof Op8XY4 o) {
            int x = vm.getRegister(o.vx()) & 0xFF;
            int y = vm.getRegister(o.vy()) & 0xFF;
            int sum = x + y;
            vm.setRegister(o.vx(), (byte) (sum & 0xFF));
            vm.setCarry((sum & 0x1FF) != (sum & 0xFF));
        } else if (opcode instanceof Op8XY5 o) {
            int x = vm.getRegister(o.vx()) & 0xFF;
            int y = vm.getRegister(o.vy()) & 0xFF;
            int diff = x - y;
            vm.setRegister(o.vx(), (byte) (diff & 0xFF));
            vm.setCarry((diff & 0x1FF) == (diff & 0xFF));
        } else if (opcode instanceof Op8XY6 o) {
            int y = vm.getRegister(o.vy()) & 0xFF;
            vm.setRegister(o.vx(), (byte) (y >> 1));
            vm.setCarry((y & 0x1) == 0x1);
        } else if (opcode instanceof Op8XY7 o) {
            int x = vm.getRegister(o.vx()) & 0xFF;
            int y = vm.getRegister(o.vy()) & 0xFF;
            int diff = y - x;
            vm.setRegister(o.vx(), (byte) (diff & 0xFF));
            vm.setCarry((diff & 0x1FF) == (diff & 0xFF));
        } else if (opcode instanceof Op8XYE o) {
            int y = vm.getRegister(o.vy()) & 0xFF;
            vm.setRegister(o.vx(), (byte) (y << 1));
            vm.setCarry((y & 0x100) == 0x100);
        } else if (opcode instanceof Op9XY0 o) {
            if (vm.getRegister(o.vx()) != vm.getRegister(o.vy())) {
                vm.setPC(vm.getPC() + 2);
            }
        } else if (opcode instanceof OpANNN o) {
            vm.setI(o.address());
        } else if (opcode instanceof OpBNNN o) {
            vm.setPC((short) (((vm.getRegister(0) & 0xFF) + o.address())) & 0xFFFF);
        } else if (opcode instanceof OpCXNN o) {
            vm.setRegister(o.vx(), (byte) (o.imm() & hal.getRand()));
        } else if (opcode instanceof OpDXYN o) {
            boolean flipped = false;
            int I = vm.getI();

            for (byte n = 0; n < o.imm(); n++) {
                byte row = vm.getByte(I + n);
                for (byte bit = 0; bit < 8; bit++) {
                    boolean flip = (row & (1 << bit)) == 1 << bit;
                    flipped = flipped | hal.draw((byte) (vm.getRegister(o.vx()) + bit),
                        (byte) (vm.getRegister(o.vy()) + n), flip);
                }
            }
            vm.setCarry(flipped);
        } else if (opcode instanceof OpEX9E o) {
            Optional<Byte> key = hal.getKey();
            if (key.isPresent() && key.get() == vm.getRegister(o.vx())) {
                vm.setPC(vm.getPC() + 2);
            }
        } else if (opcode instanceof OpFX15 o) {
            vm.setDelayTimer(vm.getRegister(o.vx()));
        } else if (opcode instanceof OpFX18 o) {
            vm.setSoundTimer(vm.getRegister(o.vx()));
        } else {
            throw new IllegalArgumentException("unsupported opcode.");
        }
    }
}
