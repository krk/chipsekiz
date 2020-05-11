# chipsekiz - CHIP-8 emulator

A [CHIP-8](https://en.wikipedia.org/wiki/CHIP-8) interpreter library in java.

## Quick Start

Emulator frontend is not implemented, library can be tested.

## Testing

Among other tests, `InterpreterTest#testRunRoms` would print framebuffer contents for test [ROMs](src/test/resources/roms/README.md) that are ran *until they halt or for max 600 cycles, whichever happens first*.

Java 14 is required.

```bash
mvn test
```

Random selection of test ROMs renders from [docs/rendered](docs/rendered):

![Chip8 emulator Logo](docs/rendered/Chip8%20emulator%20Logo%20[Garstyciuks]-cycle217.png) ![Space Invaders](docs/rendered/Space%20Invaders%20[David%20Winter]-cycle600.png) ![Blitz](docs/rendered/Blitz%20[David%20Winter]-cycle32.png)  ![Cave](docs/rendered/Cave-cycle600.png)

![Sequence Shoot](docs/rendered/Sequence%20Shoot%20[Joyce%20Weisbecker]-cycle600.png) ![Space Flight](docs/rendered/Space%20Flight-cycle600.png) ![Fishie](docs/rendered/Fishie%20[Hap,%202005]-cycle101.png) ![Lunar Lander](docs/rendered/Lunar%20Lander%20(Udo%20Pernisz,%201979)-cycle213.png)

![Particles](docs/rendered/Particle%20Demo%20[zeroZshadow,%202008]-cycle600.png) ![Reversi](docs/rendered/Reversi%20[Philip%20Baltzer]-cycle600.png) ![Tron](docs/rendered/Tron-cycle600.png) ![Tapeworm](docs/rendered/Tapeworm%20[JDR,%201999]-cycle600.png)

![Tic-Tac-Toe](docs/rendered/Tic-Tac-Toe%20[David%20Winter]-cycle164.png) ![VBrix](docs/rendered/Vertical%20Brix%20[Paul%20Robson,%201996]-cycle600.png) ![Zero Pong](docs/rendered/ZeroPong%20[zeroZshadow,%202007]-cycle600.png) ![Breakout Brix hack](docs/rendered/Breakout%20%28Brix%20hack%29%20[David%20Winter,%201997]-cycle600.png)

## Architecture

The interpreter uses a loader, a decoder, an executor and a HAL (hardware abstraction layer).

Interpreter gets a memory image from multiple layout sections which may include a character sprites section and the code(program) section. The created memory image is loaded into a new VM. Interpreter interacts with the HAL to get the keyboard status, play a sound, to draw to the screen etc.

An emulator (not implemented yet) would use the `Interpreter` "slowly" to match CPU speed expected by the programs together with a custom `IHal` implementation to provide emulated functionality for the hardware.

## Implementation

`Loader` supports building a memory image from zero or more sections and the program (ROM) itself. Sections have a start address and can include data or code, e.g. character sprites are a section. Overlapping sections are not allowed. Loader produces an image of the memory, from its input sections.

`Decoder` supports a specific ISA, currently only [CHIP-8 Wikipedia description](https://en.wikipedia.org/wiki/CHIP-8) opcodes are supported. If a value cannot be decoded as an `Opcode`, it is returned as a `DataWord` instance.

`Executor` interacts with the VM (virtual machine) and the HAL. Itself is driven by the `Interpreter`.

`VM` is what contains the memory, registers, callstack and sound and delay timers. It does not contain any execution logic, only provides the data model of the machine.

`HAL` contains every type of interaction that would require some form of hardware such as drawing to a screen, playing sound, detecting keyboard state etc. `FramebufferHal` implementation provides testability of the interpreter.

`Interpreter` would load the program and create the VM with the program and an origin. At every `Interpreter#tick`, it would run a [fetch-decode-execute](https://en.wikipedia.org/wiki/Instruction_cycle) cycle, decrease the [sound and delay timers](https://en.wikipedia.org/wiki/CHIP-8#Timers) and update the sound state.

It also has a primitive halt detector. CHIP-8 does not have a [halt instruction](https://en.wikipedia.org/wiki/Halt_and_Catch_Fire_(computing)) so the program would and should enter an infinite loop when there are no more meaningful instructions to execute. Entering an infinite loop would preserve the screen content and disable the keyboard and sound (after its timer runs out). Halt detector would not do anything other than changing the status of the interpreter to `BLOCKED` for awaiting keyboard input or `HALTED` if an instruction jumps to itself, i.e. simplest infinite loop. An infinite loop with more than one instruction would not be detected. Interpreter status is used in unit tests when interpreting the [ROMs](src/test/resources/roms).

`Interpreter` would fetch an instruction from the memory pointed by the PC (program counter) register, and PC would be incremented by 2, which is the instruction width for this architecture, same for all opcodes. This 2-byte instruction would then be decoded. If it is not a valid opcode for a given `IDecoder`, `Interpreter` would throw a *cannot execute data* exception, otherwise it would be executed with the help of `IExecutor` and `IHal`.

If there is an `ITracer` instance in the `Interpreter`, all executed opcodes would be reported to it.

`InterpreterTest` class uses the `Interpreter` to execute included ROMs until they halt or until they ran for 600 (arbitrary) instruction cycles, whichever is first.

To create a default instance of the `Interpreter`:

```java
// 1NNN instruction, which jumps to 0x200, passed as origin to the interpreter below, causing an infinite loop.
byte[] program = {0x12, 0x00};
FramebufferHal hal = new FramebufferHal(64, 32, character -> (short) 0);
Interpreter interpreter =
            new Interpreter(loader, decoder, executor, fbhal, Optional.empty(), 0x200, program,
                0x1000, CharacterSprites.DefaultLayout());

for(; /* emulator loop */ ;) {
    // 1. Forward hardware state to Hal, such as the pressed key.
    // 2. Tick the interpreter.
    // 3. Update the screen by rendering (FramebufferHal#renderFramebuffer) or directly from your own IHal implementation.
    // 4. Switch sound on or off by getting FramebufferHal#isSoundActive or in response to IHal#sound.
}
```

## Data Flow

From binary ROM to screen:

ROM -> `ILoader` -> `Interpreter#fetch` -> `IDecoder` -> `IExecutor` -> `IHal`.

## Further direction

* Add VM save and load snapshot feature - save game support for all kinds of programs.
* Implement other variations of CHIP-8, i.e. CHIP-48, hires, Super CHIP-8 etc.
* Add a console-based emulator?