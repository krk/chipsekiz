package dev.krk.chipsekiz.vm;

public interface IVMDebugger {
    void updatedByte(int address);

    void updatePC(int pc);

    void updateI(short i);
}
