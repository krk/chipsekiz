package dev.krk.chipsekiz;

import dev.krk.chipsekiz.opcodes.OpcodeOrData;

public interface IDecoder {
    OpcodeOrData decode(short value);
}
