package dev.krk.chipsekiz.superchip.hal;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;

public interface ISuperChipCharacterAddressLocator extends ICharacterAddressLocator {
    short getLargeCharacterAddress(byte character);
}
