package dev.krk.chipsekiz.superchip.sprites;

import com.google.common.collect.ImmutableList;
import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.loader.Section;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.superchip.hal.ISuperChipCharacterAddressLocator;

public class SuperChipCharacterSprites {
    private static final ISuperChipCharacterAddressLocator locator =
        new ISuperChipCharacterAddressLocator() {
            @Override public short getLargeCharacterAddress(byte character) {
                return (short) (CharacterSprites.length() + (character & 0xF) * 10);
            }

            @Override public short getCharacterAddress(byte character) {
                return (short) ((character & 0xF) * 5);
            }
        };

    // http://www.cs.columbia.edu/~sedwards/classes/2016/4840-spring/designs/Chip8.pdf
    private static final byte[] buffer =
        {(byte) 0x3C, (byte) 0x7E, (byte) 0xE7, (byte) 0xC3, (byte) 0xC3, (byte) 0xC3, (byte) 0xC3,
            (byte) 0xE7, (byte) 0x7E, (byte) 0x3C, (byte) // 0
            (byte) 0x18, (byte) 0x38, (byte) 0x58, (byte) 0x18, (byte) 0x18, (byte) 0x18,
            (byte) 0x18, (byte) 0x18, (byte) 0x18, (byte) 0x3C, (byte)  // 1
            (byte) 0x3E, (byte) 0x7F, (byte) 0xC3, (byte) 0x06, (byte) 0x0C, (byte) 0x18,
            (byte) 0x30, (byte) 0x60, (byte) 0xFF, (byte) 0xFF, (byte) // 2
            (byte) 0x3C, (byte) 0x7E, (byte) 0xC3, (byte) 0x03, (byte) 0x0E, (byte) 0x0E,
            (byte) 0x03, (byte) 0xC3, (byte) 0x7E, (byte) 0x3C, (byte) // 3
            (byte) 0x06, (byte) 0x0E, (byte) 0x1E, (byte) 0x36, (byte) 0x66, (byte) 0xC6,
            (byte) 0xFF, (byte) 0xFF, (byte) 0x06, (byte) 0x06, (byte) // 4
            (byte) 0xFF, (byte) 0xFF, (byte) 0xC0, (byte) 0xC0, (byte) 0xFC, (byte) 0xFE,
            (byte) 0x03, (byte) 0xC3, (byte) 0x7E, (byte) 0x3C, (byte) // 5
            (byte) 0x3E, (byte) 0x7C, (byte) 0xE0, (byte) 0xC0, (byte) 0xFC, (byte) 0xFE,
            (byte) 0xC3, (byte) 0xC3, (byte) 0x7E, (byte) 0x3C, (byte) // 6
            (byte) 0xFF, (byte) 0xFF, (byte) 0x03, (byte) 0x06, (byte) 0x0C, (byte) 0x18,
            (byte) 0x30, (byte) 0x60, (byte) 0x60, (byte) 0x60, (byte) // 7
            (byte) 0x3C, (byte) 0x7E, (byte) 0xC3, (byte) 0xC3, (byte) 0x7E, (byte) 0x7E,
            (byte) 0xC3, (byte) 0xC3, (byte) 0x7E, (byte) 0x3C, (byte) // 8
            (byte) 0x3C, (byte) 0x7E, (byte) 0xC3, (byte) 0xC3, (byte) 0x7F, (byte) 0x3F,
            (byte) 0x03, (byte) 0x03, (byte) 0x3E, (byte) 0x7C, (byte) // 9
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) // A - no hex fonts.
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) // B
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) // C
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) // D
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) // E
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,// F
        };

    public static byte[] get() {
        return buffer.clone();
    }

    public static ICharacterAddressLocator getAddressLocator() {
        return locator;
    }

    public static ISuperChipCharacterAddressLocator getLargeAddressLocator() {
        return locator;
    }

    public static Layout DefaultLayout() {
        return new Layout(ImmutableList.of(new Section(0, CharacterSprites.get()),
            new Section(CharacterSprites.length(), get())));
    }
}
