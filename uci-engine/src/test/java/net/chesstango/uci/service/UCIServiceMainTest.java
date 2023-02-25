/**
 *
 */
package net.chesstango.uci.service;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.service.ServiceMain;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 *
 */
public class UCIServiceMainTest {

    @Test(timeout = 3000)
    public void test_playZonda() throws IOException, InterruptedException {
        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        EngineTango engine = new EngineTango();

        ServiceMain serviceMain = new ServiceMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true));
        serviceMain.open();

        PrintStream out = new PrintStream(outputToEngine, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

        // is in ready state
        Assert.assertEquals(EngineTango.Ready.class, engine.getCurrentState().getClass());

        // uci command
        out.println("uci");
        Assert.assertEquals("id name Tango", in.readLine());
        Assert.assertEquals("id author Mauricio Coria", in.readLine());
        Assert.assertEquals("uciok", in.readLine());
        Assert.assertEquals(EngineTango.Ready.class, engine.getCurrentState().getClass());

        // isready command
        out.println("isready");
        Assert.assertEquals("readyok", in.readLine());

        Assert.assertEquals(EngineTango.Ready.class, engine.getCurrentState().getClass());

        // ucinewgame command
        out.println("ucinewgame");
        Assert.assertEquals(EngineTango.Ready.class, engine.getCurrentState().getClass());

        // isready command
        out.println("isready");
        Assert.assertEquals("readyok", in.readLine());
        Assert.assertEquals(EngineTango.Ready.class, engine.getCurrentState().getClass());

        // isrpositioneady command
        out.println("position startpos moves e2e4");
        Thread.sleep(500);
        Assert.assertEquals(EngineTango.WaitCmdGo.class, engine.getCurrentState().getClass());

        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
        Thread.sleep(500);
        Assert.assertEquals(EngineTango.WaitCmdGo.class, engine.getCurrentState().getClass());

        // quit command
        out.println("quit");

        serviceMain.waitTermination();
    }

    @Test(timeout = 3000)
    public void test_playProxy() throws IOException, InterruptedException {
        List<String> lines = null;

        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        EngineProxy engine = new EngineProxy();
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

    private String fenCode(Game board) {
        FENEncoder coder = new FENEncoder();
        board.getChessPosition().constructBoardRepresentation(coder);
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
