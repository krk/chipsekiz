package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.Decoder;
import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.loader.Loader;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.superchip.decoder.SuperChipDecoder;
import dev.krk.chipsekiz.superchip.interpreter.ISuperChipHal;
import dev.krk.chipsekiz.superchip.interpreter.SuperChipExecutor;
import dev.krk.chipsekiz.superchip.sprites.SuperChipCharacterSprites;
import dev.krk.chipsekiz.superchip.vm.SuperChipVM;
import dev.krk.chipsekiz.tracer.ITracer;
import dev.krk.chipsekiz.vm.IVirtualMachineFactory;
import dev.krk.chipsekiz.vm.VM;

import javax.annotation.Nullable;

public class InterpreterFactory {
    public static IInterpreter create(IHal hal, @Nullable ITracer tracer,
        @Nullable IDebugger debugger) {
        IDecoder decoder = new Decoder();
        IExecutor executor =
            new Executor(null, hal, CharacterSprites.getAddressLocator(), false, true);

        return create(VM::new, decoder, hal, tracer, debugger, executor);
    }

    public static IInterpreter createSuperChip(ISuperChipHal hal, @Nullable ITracer tracer,
        @Nullable IDebugger debugger) {
        IDecoder decoder = new SuperChipDecoder();
        IExecutor executor =
            new SuperChipExecutor(null, hal, SuperChipCharacterSprites.getLargeAddressLocator());

        return create(SuperChipVM::new, decoder, hal, tracer, debugger, executor);
    }

    private static IInterpreter create(IVirtualMachineFactory vmFactory, IDecoder decoder, IHal hal,
        @Nullable ITracer tracer, @Nullable IDebugger debugger, IExecutor executor) {
        Loader loader = new Loader();

        return new Interpreter(vmFactory, loader, decoder, executor, hal, tracer, debugger, 0x1000,
            CharacterSprites.DefaultLayout(), true);
    }
}
