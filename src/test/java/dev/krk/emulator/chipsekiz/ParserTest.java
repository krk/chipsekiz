package dev.krk.emulator.chipsekiz;


import com.google.common.base.Joiner;
import dev.krk.emulator.chipsekiz.opcodes.OpcodeOrData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test public void testParserInvalid() {
        Decoder decoder = new Decoder();
        Parser parser = new Parser(decoder);

        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test public void testParserValid() {
        Decoder decoder = new Decoder();
        Parser parser = new Parser(decoder);

        assertProgram(parser.parse(new byte[] {}), "");
        assertProgram(parser.parse(new byte[] {1}), "0100");
        assertProgram(parser.parse(new byte[] {1, 2}), "0102");
        assertProgram(parser.parse(new byte[] {(byte) 0xD5, 0x72}), "D572");
    }

    @ParameterizedTest @MethodSource("dev.krk.emulator.chipsekiz.Rom#Names")
    public void testParserValidRoms(String name) throws IOException {
        byte[] program = getClass().getClassLoader().getResourceAsStream(name).readAllBytes();

        Decoder decoder = new Decoder();
        Parser parser = new Parser(decoder);

        assertDoesNotThrow(() -> parser.parse(program));
    }

    private void assertProgram(List<OpcodeOrData> program, String expected) {
        assertEquals(expected,
            Joiner.on("").join(program.stream().map(od -> od.encode()).iterator()));
    }
}
