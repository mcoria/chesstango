/**
 *
 */
package net.chesstango.uci.proxy;


import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class EngineProxyTest {
    private EngineProxy engine;


    @Before
    public void setUp() {
        this.engine = new EngineProxy();
        this.engine.setLogging(true);
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

        Assert.assertEquals("Spike 1.4 (Build 84) by Volker Boehm & Ralf Schaefer, Book by Timo Klaustermeyer", input.readLine());

        // uci command
        engine.accept(new CmdUci());
        Thread.sleep(200);
        lines = readLastLine(input, "uciok"::equals);
        Assert.assertTrue(lines.stream().filter("id name Spike 1.4"::equals).findAny().isPresent());

        // isready command
        engine.accept(new CmdIsReady());
        Thread.sleep(200);
        Assert.assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        // startpos command
        engine.accept(new CmdPosition());
        Thread.sleep(200);

        // go command
        engine.accept(new CmdGo().setDepth(1));
        Thread.sleep(200);

        lines = readLastLine(input, line -> line.startsWith("bestmove"));
        Assert.assertTrue(lines.size() > 0);

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

    @Test
    @Ignore // Bug happens with MORA
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
        Assert.assertEquals("readyok", input.readLine());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Thread.sleep(200);

        String movesStr = "e1f2 a6a5 d4b5 a8a7 b5a7 h7h6 a7c8 a5a4 c8b6 f8e7 f1c4 e6e5 d1d2 g7g5 c3a4 e8g8 a4c3 g5g4 f3g4 d6d5 e4d5 d8b6 e3b6 b8c6 d5c6 e7d6 d2d6 h6h5 d6f6 g8h7 f6e5 f8e8 e5e8 f7f5 e8h5 h7g7 h5f5 g7h6 c6c7 h6g7 c7c8q g7h6";
        String[] movesStrArr = movesStr.split(" ");


        // startpos command
        engine.accept(new CmdPosition("rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8", Arrays.asList(movesStrArr)));
        Thread.sleep(200);

        // go command
        engine.accept(new CmdGo().setDepth(2));
        Thread.sleep(200);

        lines = readLastLine(input, line -> line.startsWith("bestmove"));
        Assert.assertTrue(lines.size() > 0);

        // quit command
        engine.accept(new CmdQuit());
        Thread.sleep(200);

        engine.close();
    }

}
