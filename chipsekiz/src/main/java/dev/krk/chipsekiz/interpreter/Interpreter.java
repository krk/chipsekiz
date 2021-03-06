package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.loader.ILoader;
import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.opcodes.OpFX0A;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.opcodes.OpcodeOrData;
import dev.krk.chipsekiz.tracer.ITracer;
import dev.krk.chipsekiz.vm.IVirtualMachine;
import dev.krk.chipsekiz.vm.IVirtualMachineFactory;

import javax.annotation.Nullable;

public class Interpreter implements IInterpreter {
    private IVirtualMachine vm;
    private final IDecoder decoder;
    private final IExecutor executor;
    private final ILoader loader;
    private final IHal hal;
    @Nullable private ITracer tracer;
    @Nullable private IDebugger debugger;
    private final int memorySize;
    private final Layout layout;
    private final boolean timersSixtyHertz;

    private int lastExecutedAddress;
    private Opcode lastExecutedOpcode;
    private InterpreterStatus status;
    private long prevTimersTick;
    private IVirtualMachineFactory vmFactory;

    public Interpreter(IVirtualMachineFactory vmFactory, ILoader loader, IDecoder decoder,
        IExecutor executor, IHal hal, @Nullable ITracer tracer, int origin, byte[] program,
        int memorySize, Layout layout) {
        this(vmFactory, loader, decoder, executor, hal, tracer, null, memorySize, layout, false);

        load(origin, program);
    }

    public Interpreter(IVirtualMachineFactory vmFactory, ILoader loader, IDecoder decoder,
        IExecutor executor, IHal hal, @Nullable ITracer tracer, @Nullable IDebugger debugger,
        int origin, byte[] program, int memorySize, Layout layout, boolean timersSixtyHertz) {
        this(vmFactory, loader, decoder, executor, hal, tracer, debugger, memorySize, layout,
            timersSixtyHertz);

        load(origin, program);
    }

    public Interpreter(IVirtualMachineFactory vmFactory, ILoader loader, IDecoder decoder,
        IExecutor executor, IHal hal, @Nullable ITracer tracer, @Nullable IDebugger debugger,
        int memorySize, Layout layout, boolean timersSixtyHertz) {
        this.loader = loader;
        this.decoder = decoder;
        this.executor = executor;
        this.hal = hal;
        this.tracer = tracer;
        this.debugger = debugger;
        this.memorySize = memorySize;
        this.layout = layout;
        this.timersSixtyHertz = timersSixtyHertz;
        this.status = InterpreterStatus.READY;
        this.prevTimersTick = System.nanoTime();
        this.vmFactory = vmFactory;
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
        executor.execute(opcode);
    }

    public void load(int origin, byte[] program) {
        hal.clearScreen();
        hal.sound(false);
        status = InterpreterStatus.READY;
        lastExecutedAddress = 0;
        lastExecutedOpcode = null;

        byte[] memory = loader.load(origin, program, memorySize, layout);
        this.vm = this.vmFactory.create(origin, memory, debugger);
        executor.setVM(vm);
        if (debugger != null) {
            debugger.setVM(vm);
        }
    }

    @Override public void setDebugger(IDebugger debugger) {
        this.debugger = debugger;
    }

    @Override public void setTracer(ITracer tracer) {
        this.tracer = tracer;
    }

    public void tick() {
        boolean hasSound = vm.hasSound();
        final long sixtyHertzPeriodNano = 1000000000 / 60;
        if (!timersSixtyHertz || (System.nanoTime() - prevTimersTick > sixtyHertzPeriodNano)) {
            vm.tickTimers();
            prevTimersTick = System.nanoTime();
        }

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
