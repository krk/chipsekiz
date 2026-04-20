package dev.krk.chipsekiz.opcodes;


import java.util.Objects;


public abstract class Opcode {
    private final Integer vx;
    private final Integer vy;
    private final Integer address;

    public Integer getVx() {return vx;}

    public Integer getVy() {return vy;}

    public Integer getAddress() {return address;}

    public Opcode() { this(null, null, null); }

    public Opcode(int address) { this(null, null, address); }

    public Opcode(Integer vx, Integer vy, Integer address) {
        this.address = address;
        this.vx = vx;
        this.vy = vy;
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
        return Objects.equals(vx, opcode.vx) && Objects.equals(vy, opcode.vy) && Objects
            .equals(address, opcode.address);
    }

    @Override public int hashCode() {
        return Objects.hash(vx, vy, address);
    }
}
