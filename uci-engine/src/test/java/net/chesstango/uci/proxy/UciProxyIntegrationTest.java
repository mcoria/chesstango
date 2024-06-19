package net.chesstango.uci.proxy;


import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class UciProxyIntegrationTest {
    private UciProxy engine;


    @BeforeEach
    public void setUp() {
        this.engine = new UciProxy(SpikeProxy.INSTANCE);
    }

    @Test
    public void test_OpenAndClose() throws IOException, InterruptedException {
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));
        engine.open();
        Thread.sleep(1000);
        engine.close();
    }

    @Test
    public void test_HappyPath() throws IOException, InterruptedException {
        List<String> lines = null;
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);
        BufferedReader input = new BufferedReader(new InputStreamReader(pisOutput));

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));
        engine.open();

        assertEquals("Spike 1.4 (Build 84) by Volker Boehm & Ralf Schaefer, Book by Timo Klaustermeyer", input.readLine());

        // uci command
        engine.accept(new CmdUci());
        Thread.sleep(200);
        lines = readLastLine(input, "uciok"::equals);
        assertTrue(lines.stream().filter("id name Spike 1.4"::equals).findAny().isPresent());

        // isready command
        engine.accept(new CmdIsReady());
        Thread.sleep(200);
        assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        // startpos command
        engine.accept(new CmdPosition());
        Thread.sleep(200);

        // go command
        engine.accept(new CmdGoDepth().setDepth(1));
        Thread.sleep(200);

        lines = readLastLine(input, line -> line.startsWith("bestmove"));
        assertTrue(lines.size() > 0);

        // quit command
        engine.accept(new CmdQuit());
        Thread.sleep(200);

        engine.close();
    }

    private List<String> readLastLine(BufferedReader input, Predicate<String> breakCondition) throws IOException {
        List<String> result = new ArrayList<>();
        String line = null;
        do {
            line = input.readLine();
            result.add(line);
        } while (line != null && !breakCondition.test(line));
        return result;
    }

    @Test // MORA crashes
    public void test_Crash() throws IOException, InterruptedException {
        List<String> lines = null;
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);
        BufferedReader input = new BufferedReader(new InputStreamReader(pisOutput));

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));
        engine.open();


        // uci command
        engine.accept(new CmdUci());
        Thread.sleep(200);
        lines = readLastLine(input, "uciok"::equals);

        // isready command
        engine.accept(new CmdIsReady());
        Thread.sleep(200);
        assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        String movesStr = "e1f2 a6a5 d4b5 a8a7 b5a7 h7h6 a7c8 a5a4 c8b6 f8e7 f1c4 e6e5 d1d2 g7g5 c3a4 e8g8 a4c3 g5g4 f3g4 d6d5 e4d5 d8b6 e3b6 b8c6 d5c6 e7d6 d2d6 h6h5 d6f6 g8h7 f6e5 f8e8 e5e8 f7f5 e8h5 h7g7 h5f5 g7h6 c6c7 h6g7 c7c8q g7h6";
        String[] movesStrArr = movesStr.split(" ");


        // startpos command
        engine.accept(new CmdPosition("rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8", Arrays.asList(movesStrArr)));
        Thread.sleep(200);

        // go command
        engine.accept(new CmdGoDepth().setDepth(2));
        Thread.sleep(200);

        lines = readLastLine(input, line -> line.startsWith("bestmove"));
        assertTrue(lines.size() > 0);

        // quit command
        engine.accept(new CmdQuit());
        Thread.sleep(200);

        engine.close();
    }


    @Test // Spike crashes
    @Disabled
    public void test_SpikeCrash() throws IOException, InterruptedException {
        List<String> lines = null;
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);
        BufferedReader input = new BufferedReader(new InputStreamReader(pisOutput));

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));
        engine.open();

        // uci command
        engine.accept(new CmdUci());
        Thread.sleep(200);
        lines = readLastLine(input, "uciok"::equals);

        // isready command
        engine.accept(new CmdIsReady());
        Thread.sleep(200);
        assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        String movesStr = "a7a6 b5e2 d7b6 a4a5 b6d7 f3e1 d7b8 c3b1 f6e4 h2h3 b8c6 h3h4 d8h4 g2g3 h4h3 g3g4 d6d5 g4g5 e4g5 f2f3 c6d4 f3f4 e5f4 f1f4 h3g3 g1f1 c8h3 e1g2 h3g2 f1g1";
        List<String> moveList = Arrays.asList(movesStr.split(" "));

        for (int i = 0; i < 16; i++) {
            // startpos command
            engine.accept(new CmdPosition("r1bqkb1r/pp1n1ppp/3p1n2/1Bp1p3/P3P3/2N2N2/1PPP1PPP/R1BQ1RK1 b kq - 1 6", moveList.subList(0, i * 2)));
            Thread.sleep(200);

            // go command
            engine.accept(new CmdGoDepth().setDepth(1));
            Thread.sleep(200);

            lines = readLastLine(input, line -> line.startsWith("bestmove"));
            assertFalse(lines.isEmpty());
        }

        // quit command
        engine.accept(new CmdQuit());
        Thread.sleep(200);

        engine.close();
    }


    @Test // Spike crashes
    @Disabled
    public void test_SpikeCrash01() throws IOException, InterruptedException {
        List<String> lines = null;
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);
        BufferedReader input = new BufferedReader(new InputStreamReader(pisOutput));

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));
        engine.open();

        // uci command
        engine.accept(new CmdUci());
        Thread.sleep(200);
        lines = readLastLine(input, "uciok"::equals);

        // isready command
        engine.accept(new CmdIsReady());
        Thread.sleep(200);
        assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        String movesStr = "c4d3 f8b4 e1g1 b4c3 b2c3 e8g8 c1a3 d8c7 a3f8 c7h2 g1h2 f6g4 h2g3 d7f6 f8d6 c8b7 d6c5 a8d8 c5a7 b5b4 a7b6 b4c3 b6d8 h7h6 d1b3 c3c2 b3b7 c2c1n a1c1 f6d5 g3g4 f7f5 g4h5 d5f4 e3f4 c6c5 h5g6 g8f8";
        List<String> moveList = Arrays.asList(movesStr.split(" "));

        for (int i = 0; i < 20; i++) {
            // startpos command
            engine.accept(new CmdPosition("r1bqkb1r/p2n1ppp/2p1pn2/1p6/2BP4/2N1PN2/PP3PPP/R1BQK2R w KQkq b6 0 8", moveList.subList(0, i * 2)));
            Thread.sleep(200);

            // go command
            engine.accept(new CmdGoDepth().setDepth(2));
            Thread.sleep(200);

            lines = readLastLine(input, line -> line.startsWith("bestmove"));
            assertFalse(lines.isEmpty());
        }

        // quit command
        engine.accept(new CmdQuit());
        Thread.sleep(200);

        engine.close();
    }

}
