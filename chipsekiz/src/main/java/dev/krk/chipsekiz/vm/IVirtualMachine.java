package dev.krk.chipsekiz.vm;

public interface IVirtualMachine {
    int getPC();

    void setPC(int pc);

    short getI();

    void setI(short i);

    int getOrigin();

    int getMemorySize();

    byte getRegister(int i);

    void setRegister(int i, byte value);

    boolean hasCarry();

    void setCarry(boolean carry);

    void push(int value);

    int pop();

    void tickTimers();

    byte getDelayTimer();

    void setDelayTimer(byte value);

    byte getSoundTimer();

    void setSoundTimer(byte value);

    void setByte(int address, byte value);

    byte getByte(int address);
}
