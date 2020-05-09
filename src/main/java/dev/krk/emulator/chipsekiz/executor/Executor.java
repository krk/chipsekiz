package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Op00EE;
import dev.krk.emulator.chipsekiz.opcodes.Op0NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op2NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op6XNN;
import dev.krk.emulator.chipsekiz.opcodes.OpFX15;
import dev.krk.emulator.chipsekiz.opcodes.OpFX18;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.vm.VM;

public class Executor implements IExecutor {
    @Override public void execute(VM vm, IHal hal, Opcode opcode) {
        if (opcode instanceof Op00EE) {
            vm.setPC(vm.pop());
        } else if (opcode instanceof Op0NNN o) {
            vm.push(vm.getPC());
            vm.setPC(o.address());
        } else if (opcode instanceof Op1NNN o) {
            vm.setPC(o.address());
        } else if (opcode instanceof Op2NNN o) {
            vm.push(vm.getPC());
            vm.setPC(o.address());
        } else if (opcode instanceof Op6XNN o) {
            vm.setRegister(o.vx(), o.imm());
        } else if (opcode instanceof OpFX15 o) {
            vm.setDelayTimer(vm.getRegister(o.vx()));
        } else if (opcode instanceof OpFX18 o) {
            vm.setSoundTimer(vm.getRegister(o.vx()));
        } else {
            throw new IllegalArgumentException("unsupported opcode.");
        }
    }
}
