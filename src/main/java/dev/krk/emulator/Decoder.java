package dev.krk.emulator;



import java.util.Optional;

public class Decoder {
    public Optional<Opcode> decode(short value) {
        int b0 = (value & 0xF000) >> 12;
        switch (b0) {
            case 0:
                if (value == 0x00E0) {
                    return Optional.of(new Opcode(OpcodeType.Op00E0));
                }
                if (value == 0x00EE) {
                    return Optional.of(new Opcode(OpcodeType.Op00EE));
                }

                return Optional.of(new Opcode(OpcodeType.Op0NNN, value & 0xFFF));
            case 1:
                return Optional.of(new Opcode(OpcodeType.Op1NNN, value & 0xFFF));
            case 2:
                return Optional.of(new Opcode(OpcodeType.Op2NNN, value & 0xFFF));
            case 3:
                return Optional.of(new Opcode(OpcodeType.Op3XNN, new Register((value & 0xF00) >> 8),
                    value & 0xFF));
        }

        return Optional.empty();
    }
}
