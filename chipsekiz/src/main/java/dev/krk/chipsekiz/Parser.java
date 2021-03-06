package dev.krk.chipsekiz;

import com.google.common.collect.ImmutableList;
import dev.krk.chipsekiz.opcodes.OpcodeOrData;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Parser {
    private final IDecoder decoder;

    public Parser(IDecoder decoder) {
        this.decoder = decoder;
    }

    public List<OpcodeOrData> parse(byte[] program) {
        // All opcodes have the same length, 16 bits.
        checkNotNull(program, "program");

        boolean padEnd = program.length % 2 != 0;
        ArrayList<OpcodeOrData> opcodes = new ArrayList<>();
        for (int i = 0; i < program.length; i += 2) {
            int b0 = (program[i] << 8) & 0xFFFF;
            int b1 = padEnd && (i + 1 == program.length) ? 0 : program[i + 1] & 0xFF;
            short value = (short) ((b0 | b1) & 0xFFFF);

            opcodes.add(decoder.decode(value));
        }
        return ImmutableList.copyOf(opcodes);
    }
}
