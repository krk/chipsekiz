package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.vm.IVirtualMachine;
import dev.krk.chipsekiz.vm.VM;

public class ExecutorTestContext {
    private IVirtualMachine vm;
    private IHal hal;
    private ICharacterAddressLocator cal;
    private IExecutor executor;

    public ExecutorTestContext(VM vm, IHal hal, ICharacterAddressLocator cal, IExecutor executor) {
        this.vm = vm;
        this.hal = hal;
        this.cal = cal;
        this.executor = executor;
    }

    public IVirtualMachine vm() {return vm;}

    public IHal hal() {return hal;}

    public ICharacterAddressLocator cal() {return cal;}

    public IExecutor executor() {return executor;}
}
