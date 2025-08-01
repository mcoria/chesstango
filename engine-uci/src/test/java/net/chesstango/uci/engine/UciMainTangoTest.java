package net.chesstango.uci.engine;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class UciMainTangoTest {

    private ExecutorService executorService;


    @BeforeEach
    public void setup(){
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void end(){
        executorService.shutdown();
        try {
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    public void test_playTango() throws IOException, InterruptedException {
        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        UciTango engine = new UciTango();

        UciMain uciMain = new UciMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true));
        executorService.submit(uciMain);

        PrintStream out = new PrintStream(outputToEngine, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

        // uci command
        out.println("uci");
        assertTrue(in.readLine().startsWith("id name Tango"));
        assertEquals("id author Mauricio Coria", in.readLine());
        assertEquals("option name PolyglotPath type string default <empty>", in.readLine());
        assertEquals("option name SyzygyDirectory type string default <empty>", in.readLine());
        assertEquals("uciok", in.readLine());

        // isready command
        out.println("isready");
        assertEquals("readyok", in.readLine());

        // ucinewgame command
        out.println("ucinewgame");

        // isready command
        out.println("isready");
        assertEquals("readyok", in.readLine());

        // isrpositioneady command
        out.println("position startpos moves e2e4");

        // quit command
        out.println("quit");

        while (uciMain.isRunning()){
            Thread.sleep(200);
        };
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
