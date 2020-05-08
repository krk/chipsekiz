package dev.krk.emulator.chipsekiz;

import dev.krk.emulator.chipsekiz.opcodes.OpcodeOrData;

public interface IDecoder {
    OpcodeOrData decode(short value);
}
