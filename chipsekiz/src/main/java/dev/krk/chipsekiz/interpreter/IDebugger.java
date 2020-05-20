package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.vm.IVMDebugger;
import dev.krk.chipsekiz.vm.IVirtualMachine;


public interface IDebugger extends IVMDebugger {
    void setVM(IVirtualMachine vm);

    void closeDebuggerWindow();
}
