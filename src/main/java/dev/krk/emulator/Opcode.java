package dev.krk.emulator;

import java.util.Optional;

public class Opcode {
    private OpcodeType type;
    private Optional<Register> vx;
    private Optional<Register> vy;
    private Optional<Integer> address;

    public OpcodeType getType() {return type;}

    public Optional<Register> getVx() {return vx;}

    public Optional<Register> getVy() {return vy;}

    public Optional<Integer> getAddress() {return address;}

    public Opcode(OpcodeType type) {
        this.type = type;
        this.address = Optional.empty();
        this.vx = Optional.empty();
        this.vy = Optional.empty();
    }

    public Opcode(OpcodeType type, int address) {
        this.type = type;
        this.address = Optional.of(address);
        this.vx = Optional.empty();
        this.vy = Optional.empty();
    }

    public Opcode(OpcodeType type, Register vx, int address) {
        this.type = type;
        this.address = Optional.of(address);
        this.vx = Optional.of(vx);
        this.vy = Optional.empty();
    }
}
