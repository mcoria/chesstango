/**
 *
 */
package net.chesstango.uci.service;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class ServiceMainTest {

    @Test(timeout = 3000)
    public void test_playZonda() throws IOException, InterruptedException {
        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        EngineTango engine = new EngineTango();

        ServiceMain serviceMain = new ServiceMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true));
        serviceMain.open();

        PrintStream out = new PrintStream(outputToEngine, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));


        // uci command
        out.println("uci");
        Assert.assertEquals("id name Tango", in.readLine());
        Assert.assertEquals("id author Mauricio Coria", in.readLine());
        Assert.assertEquals("uciok", in.readLine());

        // isready command
        out.println("isready");
        Assert.assertEquals("readyok", in.readLine());

        // ucinewgame command
        out.println("ucinewgame");

        // isready command
        out.println("isready");
        Assert.assertEquals("readyok", in.readLine());

        // isrpositioneady command
        out.println("position startpos moves e2e4");

        // quit command
        out.println("quit");

        serviceMain.waitTermination();
    }

    @Test(timeout = 4000)
    public void test_playProxy() throws IOException, InterruptedException {
        List<String> lines = null;

        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        EngineProxy engine = new EngineProxy(ProxyConfig.loadEngineConfig("Spike"));
        engine.setLogging(true);

        ServiceMain serviceMain = new ServiceMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true));
        serviceMain.open();

        PrintStream out = new PrintStream(outputToEngine, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

        // uci command
        out.println("uci");
        Thread.sleep(200);
        lines = readLastLine(in, "uciok"::equals);
        Assert.assertTrue(lines.stream().filter("id name Spike 1.4"::equals).findAny().isPresent());

        // isready command
        out.println("isready");
        Thread.sleep(200);
        Assert.assertEquals("readyok", in.readLine());

        // ucinewgame command
        out.println("ucinewgame");
        Thread.sleep(200);

        // isready command
        out.println("isready");
        Thread.sleep(200);

        // isrpositioneady command
        out.println("position startpos");
        Thread.sleep(200);

        // go command
        out.println("go depth 1");
        Thread.sleep(200);

        lines = readLastLine(in, line -> line.startsWith("bestmove"));
        Assert.assertTrue(lines.size() > 0);

        // quit command
        out.println("quit");
        Thread.sleep(200);

        serviceMain.waitTermination();
    }

    private String fenCode(Game game) {
        FENEncoder coder = new FENEncoder();
        game.getChessPosition().constructBoardRepresentation(coder);
        return coder.getChessRepresentation();
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
