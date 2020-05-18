package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.Decoder;
import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.loader.Loader;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.tracer.ITracer;

import javax.annotation.Nullable;

public class InterpreterFactory {
    public static Interpreter create(IHal hal,
        @Nullable ICharacterAddressLocator characterAddressLocator, @Nullable ITracer tracer,
        @Nullable IDebugger debugger) {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor(false, true);

        return new Interpreter(loader, decoder, executor, hal, characterAddressLocator, tracer,
            debugger, 0x1000, CharacterSprites.DefaultLayout(), true);
    }
}
