package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.sprites.CharacterSprites;

import java.io.IOException;


public class ChipVariationFactory {
    public static IChipVariation createChip8() {
        byte[] program = null;
        try {
            // Load demo ROM at start.
            program = ChipVariationFactory.class.getClassLoader()
                .getResourceAsStream("demo/chipsekiz-demo.ch8").readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ChipVariation("CHIP-8", hal -> InterpreterFactory
            .create(hal, CharacterSprites.getAddressLocator(), null, null,
                new Executor(true, true)), 64, 32, 0x200, program);
    }

    public static IChipVariation createChip48() {
        byte[] program = null;
        try {
            // Load demo ROM at start.
            program = ChipVariationFactory.class.getClassLoader()
                .getResourceAsStream("demo/chipsekiz-demo.ch8").readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ChipVariation("S-CHIP",
            hal -> InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null, null),
            64, 32, 0x200, program);
    }
}
