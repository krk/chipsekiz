package dev.krk.chipsekiz;

public class Rom {
    private static final String[] SuperChipNames =
        {"superchip-roms/ALIEN", "superchip-roms/ANT", "superchip-roms/BLINKY",
            "superchip-roms/CAR", "superchip-roms/FIELD", "superchip-roms/JOUST",
            "superchip-roms/PIPER", "superchip-roms/RACE", "superchip-roms/SCTEST",
            "superchip-roms/SPACEFIG", "superchip-roms/UBOAT", "superchip-roms/WORM3",};

    private static final String[] Names = {"roms/BC_test.ch8", "roms/chipsekiz-demo.ch8",

        "roms/1dcell.ch8", "roms/chipwar.ch8", "roms/danm8ku.ch8", "roms/glitchGhost.ch8",
        "roms/horseWorldOnline.ch8", "roms/octojam6title.ch8", "roms/piper.ch8", "roms/RPS.ch8",
        "roms/snake.ch8", "roms/spacejam.ch8",

        "roms/15 Puzzle [Roger Ivie] (alt).ch8", "roms/Addition Problems [Paul C. Moews].ch8",
        "roms/Airplane.ch8", "roms/Animal Race [Brian Astle].ch8",
        "roms/Astro Dodge [Revival Studios, 2008].ch8", "roms/Biorhythm [Jef Winsor].ch8",
        "roms/Blinky [Hans Christian Egeberg, 1991].ch8",
        "roms/Blinky [Hans Christian Egeberg] (alt).ch8", "roms/Blitz [David Winter].ch8",
        "roms/BMP Viewer - Hello (C8 example) [Hap, 2005].ch8",
        "roms/Bowling [Gooitzen van der Wal].ch8",
        "roms/Breakout (Brix hack) [David Winter, 1997].ch8",
        "roms/Breakout [Carmelo Cortez, 1979].ch8", "roms/Brick (Brix hack, 1990).ch8",
        "roms/Brix [Andreas Gustafsson, 1990].ch8", "roms/Cave.ch8",
        "roms/Chip8 emulator Logo [Garstyciuks].ch8", "roms/Chip8 Picture.ch8",
        "roms/Clock Program [Bill Fisher, 1981].ch8",
        "roms/Coin Flipping [Carmelo Cortez, 1978].ch8", "roms/Connect 4 [David Winter].ch8",
        "roms/Craps [Camerlo Cortez, 1978].ch8", "roms/Deflection [John Fort].ch8",
        "roms/Delay Timer Test [Matthew Mikolay, 2010].ch8",
        "roms/Division Test [Sergey Naydenov, 2010].ch8", "roms/Figures.ch8", "roms/Filter.ch8",
        "roms/Fishie [Hap, 2005].ch8", "roms/Framed MK1 [GV Samways, 1980].ch8",
        "roms/Framed MK2 [GV Samways, 1980].ch8", "roms/Guess [David Winter] (alt).ch8",
        "roms/Guess [David Winter].ch8", "roms/Hidden [David Winter, 1996].ch8",
        "roms/Hi-Lo [Jef Winsor, 1978].ch8", "roms/IBM Logo.ch8",
        "roms/Jumping X and O [Harry Kleinberg, 1977].ch8",
        "roms/Kaleidoscope [Joseph Weisbecker, 1978].ch8", "roms/Keypad Test [Hap, 2006].ch8",
        "roms/Landing.ch8", "roms/Life [GV Samways, 1980].ch8",
        "roms/Lunar Lander (Udo Pernisz, 1979).ch8",
        "roms/Mastermind FourRow (Robert Lindley, 1978).ch8",
        "roms/Maze (alt) [David Winter, 199x].ch8", "roms/Maze [David Winter, 199x].ch8",
        "roms/Merlin [David Winter].ch8", "roms/Minimal game [Revival Studios, 2007].ch8",
        "roms/Missile [David Winter].ch8", "roms/Most Dangerous Game [Peter Maruhnic].ch8",
        "roms/Nim [Carmelo Cortez, 1978].ch8", "roms/Paddles.ch8",
        "roms/Particle Demo [zeroZshadow, 2008].ch8",
        "roms/Pong 2 (Pong hack) [David Winter, 1997].ch8", "roms/Pong (alt).ch8",
        "roms/Pong [Paul Vervalin, 1990].ch8", "roms/Programmable Spacefighters [Jef Winsor].ch8",
        "roms/Puzzle.ch8", "roms/Random Number Test [Matthew Mikolay, 2010].ch8",
        "roms/Reversi [Philip Baltzer].ch8", "roms/Rocket [Joseph Weisbecker, 1978].ch8",
        "roms/Rocket Launcher.ch8", "roms/Rocket Launch [Jonas Lindstedt].ch8",
        "roms/Rush Hour [Hap, 2006] (alt).ch8", "roms/Rush Hour [Hap, 2006].ch8",
        "roms/Russian Roulette [Carmelo Cortez, 1978].ch8",
        "roms/Sequence Shoot [Joyce Weisbecker].ch8",
        "roms/Shooting Stars [Philip Baltzer, 1978].ch8",
        "roms/Sierpinski [Sergey Naydenov, 2010].ch8", "roms/Slide [Joyce Weisbecker].ch8",
        "roms/Soccer.ch8", "roms/Space Flight.ch8",
        "roms/Space Intercept [Joseph Weisbecker, 1978].ch8",
        "roms/Space Invaders [David Winter] (alt).ch8", "roms/Space Invaders [David Winter].ch8",
        "roms/Spooky Spot [Joseph Weisbecker, 1978].ch8",
        "roms/SQRT Test [Sergey Naydenov, 2010].ch8", "roms/Squash [David Winter].ch8",
        "roms/Stars [Sergey Naydenov, 2010].ch8", "roms/Submarine [Carmelo Cortez, 1978].ch8",
        "roms/Sum Fun [Joyce Weisbecker].ch8", "roms/Syzygy [Roy Trevino, 1990].ch8",
        "roms/Tank.ch8", "roms/Tapeworm [JDR, 1999].ch8", "roms/Tetris [Fran Dachille, 1991].ch8",
        "roms/Tic-Tac-Toe [David Winter].ch8", "roms/Timebomb.ch8",
        "roms/Trip8 Demo (2008) [Revival Studios].ch8", "roms/Tron.ch8",
        "roms/UFO [Lutz V, 1992].ch8", "roms/Vertical Brix [Paul Robson, 1996].ch8",
        "roms/Wall [David Winter].ch8", "roms/Worm V4 [RB-Revival Studios, 2007].ch8",
        "roms/X-Mirror.ch8", "roms/Zero Demo [zeroZshadow, 2007].ch8",
        "roms/ZeroPong [zeroZshadow, 2007].ch8",

        "roms/15PUZZLE", "roms/KALEID", "roms/VERS", "roms/WIPEOFF"};

    public static String[] Names() {
        return Names;
    }

    public static String[] SuperChipNames() {
        return SuperChipNames;
    }
}
