package dev.krk.emulator.chipsekiz.opcodes;


import java.util.Optional;

public abstract class Opcode {
    private final Optional<Integer> vx;
    private final Optional<Integer> vy;
    private final Optional<Integer> address;

    public Optional<Integer> getVx() {return vx;}

    public Optional<Integer> getVy() {return vy;}

    public Optional<Integer> getAddress() {return address;}

    public Opcode() {
        this.address = Optional.empty();
        this.vx = Optional.empty();
        this.vy = Optional.empty();
    }

    public Opcode(int address) {
        this(Optional.empty(), Optional.empty(), Optional.of(address));
    }

    public Opcode(Optional<Integer> vx, Optional<Integer> vy, Optional<Integer> address) {
        this.address = address;
        this.vx = vx;
        this.vy = vy;
    }

    public abstract short getValue();

    public abstract String encode();

    @Override public String toString() {
        return encode();
    }
}
