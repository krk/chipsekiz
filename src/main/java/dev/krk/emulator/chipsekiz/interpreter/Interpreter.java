package dev.krk.emulator.chipsekiz.interpreter;

import dev.krk.emulator.chipsekiz.IDecoder;
import dev.krk.emulator.chipsekiz.executor.IExecutor;
import dev.krk.emulator.chipsekiz.loader.ILoader;
import dev.krk.emulator.chipsekiz.loader.Layout;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.opcodes.OpcodeOrData;
import dev.krk.emulator.chipsekiz.vm.VM;

import static com.google.common.base.Preconditions.checkState;

public class Interpreter {
    private final VM vm;
    private final IDecoder decoder;
    private final IExecutor executor;
    private final IHal hal;

    public Interpreter(ILoader loader, IDecoder decoder, IExecutor executor, IHal hal, int origin,
        byte[] program, int memorySize, Layout layout) {
        this.hal = hal;

        byte[] memory = loader.load(origin, program, memorySize, layout);
        this.vm = new VM(origin, memory);
        this.decoder = decoder;
        this.executor = executor;
    }

    private short fetch() {
        int pc = vm.getPC();
        short instruction = vm.getShort(pc);
        vm.setPC(pc + 2);
        return instruction;
    }

    private OpcodeOrData decode(short instruction) {
        return decoder.decode(instruction);
    }

    private void execute(Opcode opcode) {
        executor.execute(vm, hal, opcode);
    }

    public void tick() {
        boolean hasSound = vm.hasSound();
        vm.tickTimers();

        short instruction = fetch();
        OpcodeOrData od = decode(instruction);
        checkState(od.getKind() == OpcodeOrData.Kind.OPCODE, "cannot execute data.");
        execute(od.opcode());

        if (hasSound != vm.hasSound()) {
            hal.sound(vm.hasSound());
        }
    }
}
