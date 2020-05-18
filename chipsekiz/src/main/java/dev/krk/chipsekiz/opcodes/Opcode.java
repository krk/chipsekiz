package dev.krk.chipsekiz.opcodes;


import com.google.common.base.Objects;

import javax.annotation.Nullable;

import java.util.Optional;


public abstract class Opcode {
    private final Integer vx;
    private final Integer vy;
    private final Integer address;
    private final OpcodeKind kind;

    public Optional<Integer> getVx() {return Optional.ofNullable(vx);}

    public Optional<Integer> getVy() {return Optional.ofNullable(vy);}

    public Optional<Integer> getAddress() {return Optional.ofNullable(address);}

    public Opcode() { this(null, null, null); }

    public Opcode(int address) { this(null, null, address); }

    public Opcode(@Nullable Integer vx, @Nullable Integer vy, @Nullable Integer address) {
        this.address = address;
        this.vx = vx;
        this.vy = vy;

        // No type switch in java yet, use kind for switching.
        OpcodeKind kind;
        try {
            kind = OpcodeKind.valueOf(getClass().getSimpleName());
        } catch (IllegalArgumentException e) {
            kind = OpcodeKind.Other;
        }
        this.kind = kind;
    }

    public abstract short getValue();

    public abstract String encode();

    @Override public String toString() {
        return encode();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Opcode opcode = (Opcode) o;
        return Objects.equal(vx, opcode.vx) && Objects.equal(vy, opcode.vy) && Objects
            .equal(address, opcode.address);
    }

    @Override public int hashCode() {
        return Objects.hashCode(vx, vy, address);
    }

    public OpcodeKind getKind() {
        return kind;
    }
}
