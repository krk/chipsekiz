package dev.krk.chipsekiz;

import dev.krk.chipsekiz.opcodes.Word;

public interface IDecoder {
    Word decode(short value);
}
