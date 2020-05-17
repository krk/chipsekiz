package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.vm.IVirtualMachine;

public interface IDebugger {
    void setVM(IVirtualMachine vm);
}
