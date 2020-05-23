package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.vm.IVirtualMachine;

public class Debugger implements IDebugger {
    private DebuggerWindow window;

    @Override public void setVM(IVirtualMachine vm) {
        window = new DebuggerWindow(vm);
    }

    @Override public void closeDebuggerWindow() {
        if (window == null) {
            return;
        }
        window.dispose();
        window = null;
    }

    @Override public void updatedByte(int address) {
        if (window == null) {
            return;
        }
        window.requestRepaint(VMUpdateKind.Address, address);
    }

    @Override public void updatePC(int pc) {
        if (window == null) {
            return;
        }
        window.requestRepaint(VMUpdateKind.PC, pc);
    }

    @Override public void updateI(short i) {
        if (window == null) {
            return;
        }
        window.requestRepaint(VMUpdateKind.I, i);
    }
}
