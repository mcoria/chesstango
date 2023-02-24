/**
 *
 */
package net.chesstango.uci.proxy;


import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import net.chesstango.uci.proxy.EngineProxy;
import org.junit.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class UCIServiceProxyTest {
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

}
