package dev.krk.emulator;



import java.util.Optional;

public class Decoder {
    public Optional<Opcode> decode(short value) {
        int b0 = (value & 0xF000) >> 12;
        switch (b0) {
            case 1:
                int address = value & 0xFFF;
                return Optional.of(new Opcode(OpcodeType.Op1NNN, address));
        }

        return Optional.empty();
    }
}

