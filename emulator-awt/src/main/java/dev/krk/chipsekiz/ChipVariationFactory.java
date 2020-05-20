package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import java.io.IOException;


public class ChipVariationFactory {
    public static IChipVariation createChip8(IHal hal) {
        byte[] program = null;
        try {
            // Load demo ROM at start.
            program = App.class.getClassLoader().getResourceAsStream("demo/chipsekiz-demo.ch8")
                .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Interpreter interpreter =
            InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null, null);

        return new ChipVariation(interpreter, 64, 32, 0x200, program);
    }
}
