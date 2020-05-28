package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.superchip.interpreter.ISuperChipHal;

import java.io.IOException;


public class ChipVariationFactory {
    public static IChipVariation<IHal> createChip8() {
        byte[] program = null;
        try {
            // Load demo ROM at start.
            program = ChipVariationFactory.class.getClassLoader()
                .getResourceAsStream("demo/chipsekiz-demo.ch8").readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ChipVariation<IHal>("CHIP-8", hal -> InterpreterFactory.create(hal, null, null),
            64, 32, 0x200, program);
    }

    public static IChipVariation<ISuperChipHal> createSuperChip8() {
        byte[] program = null;
        try {
            // Load demo ROM at start.
            program = ChipVariationFactory.class.getClassLoader()
                .getResourceAsStream("demo/chipsekiz-demo.ch8").readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ChipVariation<ISuperChipHal>("S-CHIP",
            hal -> InterpreterFactory.createSuperChip(hal, null, null), 64, 32, 0x200, program);
    }
}
