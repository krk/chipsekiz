package dev.krk.chipsekiz.superchip.decoder;


import dev.krk.chipsekiz.Decoder;
import dev.krk.chipsekiz.IDecoder;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.opcodes.OpcodeOrData;
import dev.krk.chipsekiz.superchip.opcodes.Op00CN;
import dev.krk.chipsekiz.superchip.opcodes.Op00FB;
import dev.krk.chipsekiz.superchip.opcodes.Op00FC;
import dev.krk.chipsekiz.superchip.opcodes.Op00FD;
import dev.krk.chipsekiz.superchip.opcodes.Op00FE;
import dev.krk.chipsekiz.superchip.opcodes.Op00FF;
import dev.krk.chipsekiz.superchip.opcodes.OpDXY0;
import dev.krk.chipsekiz.superchip.opcodes.OpFX30;
import dev.krk.chipsekiz.superchip.opcodes.OpFX75;
import dev.krk.chipsekiz.superchip.opcodes.OpFX85;

import java.util.HashMap;

public class SuperChipDecoder extends Decoder implements IDecoder {
    private final HashMap<Short, OpcodeOrData> cache;

    public SuperChipDecoder() {
        cache = new HashMap<>();
    }

    @Override public OpcodeOrData decode(short value) {
        OpcodeOrData cached = cache.get(value);
        if (cached != null) {
            return cached;
        }

        int b0 = (value & 0xF000) >> 12;
        Opcode opcode = switch (b0) {
            case 0 -> switch (value & 0xFFF0) {
                case 0x00F0 -> switch (value) {
                    case 0x00FB -> new Op00FB();
                    case 0x00FC -> new Op00FC();
                    case 0x00FD -> new Op00FD();
                    case 0x00FE -> new Op00FE();
                    case 0x00FF -> new Op00FF();
                    default -> null;
                };
                case 0x00C0 -> (new Op00CN((byte) (value & 0xF)));
                default -> null;
            };
            case 0xD -> switch (value & 0xF00F) {
                case 0xD000 -> new OpDXY0((value & 0xF00) >> 8, (value & 0xF0) >> 4);
                default -> null;
            };
            case 0xF -> switch (value & 0xFF) {
                case 0x30 -> (new OpFX30((value & 0xF00) >> 8));
                case 0x75 -> (new OpFX75((value & 0xF00) >> 8));
                case 0x85 -> (new OpFX85((value & 0xF00) >> 8));
                default -> null;
            };
            default -> null;
        };

        OpcodeOrData opcodeOrData =
            opcode == null ? super.decode(value) : OpcodeOrData.ofOpcode(opcode);
        cache.put(value, opcodeOrData);
        return opcodeOrData;
    }
}
