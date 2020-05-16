package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.opcodes.OpFX0A;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.opcodes.OpcodeOrData;
import dev.krk.chipsekiz.tracer.ITracer;
import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.loader.ILoader;
import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.vm.VM;

import javax.annotation.Nullable;

public class Interpreter {
    private final VM vm;
    private final IDecoder decoder;
    private final IExecutor executor;
    private final IHal hal;
    private final ITracer tracer;

    private int lastExecutedAddress;
    private Opcode lastExecutedOpcode;
    private InterpreterStatus status;

    public Interpreter(ILoader loader, IDecoder decoder, IExecutor executor, IHal hal,
        @Nullable ITracer tracer, int origin, byte[] program, int memorySize, Layout layout) {
        this.hal = hal;

        byte[] memory = loader.load(origin, program, memorySize, layout);
        this.vm = new VM(origin, memory);
        this.decoder = decoder;
        this.executor = executor;
        this.tracer = tracer;
        this.status = InterpreterStatus.READY;
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

        int pc = vm.getPC();
        short instruction = fetch();

        OpcodeOrData od = decode(instruction);
        if (od.getKind() != OpcodeOrData.Kind.OPCODE) {
            throw new IllegalStateException(
                String.format("cannot execute data at %04X: %s", pc, od.encode()));
        }

        execute(od.opcode());

        if (hasSound != vm.hasSound()) {
            hal.sound(vm.hasSound());
        }

        if (tracer != null) {
            tracer.trace(pc, od.opcode());
        }

        if (lastExecutedAddress == pc && lastExecutedOpcode != null && lastExecutedOpcode
            .equals(od.opcode())) {
            // Either jump-to-self or waiting for key.
            if (od.opcode() instanceof OpFX0A) {
                status = InterpreterStatus.BLOCKED;
            } else {
                status = InterpreterStatus.HALTED;
            }
        } else {
            status = InterpreterStatus.READY;
        }

        lastExecutedAddress = pc;
        lastExecutedOpcode = od.opcode();
    }

    public InterpreterStatus getStatus() {
        return status;
    }
}
