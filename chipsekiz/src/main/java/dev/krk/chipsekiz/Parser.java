package dev.krk.chipsekiz;

import dev.krk.chipsekiz.opcodes.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {
    private final IDecoder decoder;

    public Parser(IDecoder decoder) {
        this.decoder = decoder;
    }

    public List<Word> parse(byte[] program) {
        // All opcodes have the same length, 16 bits.
        Objects.requireNonNull(program, "program");

        boolean padEnd = program.length % 2 != 0;
        ArrayList<Word> opcodes = new ArrayList<>();
        for (int i = 0; i < program.length; i += 2) {
            int b0 = (program[i] << 8) & 0xFFFF;
            int b1 = padEnd && (i + 1 == program.length) ? 0 : program[i + 1] & 0xFF;
            short value = (short) ((b0 | b1) & 0xFFFF);

            opcodes.add(decoder.decode(value));
        }
        return List.copyOf(opcodes);
    }
}
