package dev.krk.chipsekiz.vm;

public interface IVirtualMachineFactory {
    IVirtualMachine create(int origin, byte[] memory, IVMDebugger debugger);
}
