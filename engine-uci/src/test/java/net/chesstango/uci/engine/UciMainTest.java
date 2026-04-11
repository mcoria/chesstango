package net.chesstango.uci.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class UciMainTest {

    private ExecutorService executorService;


    @BeforeEach
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void end() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    public void test_playTango() {
        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        try (UciMain uciMain = new UciMain(new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true))) {
            executorService.submit(uciMain);

            PrintStream out = new PrintStream(outputToEngine, true);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

            // uci command
            out.println("uci");
            assertTrue(in.readLine().startsWith("id name Tango"));
            assertEquals("id author Mauricio Coria", in.readLine());
            assertEquals("option name PolyglotFile type string default <empty>", in.readLine());
            assertEquals("option name SyzygyPath type string default <empty>", in.readLine());
            assertEquals("option name Hash type spin default 32 minValue 1 maxValue 64", in.readLine());
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

            while (uciMain.isRunning()) {
                Thread.sleep(200);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    //@Disabled
    public void test_setOptions() {
        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();


        try (UciMain uciMain = new UciMain(new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true))) {
            executorService.submit(uciMain);

            PrintStream out = new PrintStream(outputToEngine, true);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

            // uci command
            out.println("uci");
            assertTrue(in.readLine().startsWith("id name Tango"));
            assertEquals("id author Mauricio Coria", in.readLine());
            assertEquals("option name PolyglotFile type string default <empty>", in.readLine());
            assertEquals("option name SyzygyPath type string default <empty>", in.readLine());
            assertEquals("option name Hash type spin default 32 minValue 1 maxValue 64", in.readLine());
            assertEquals("uciok", in.readLine());

            // setoption command
            out.println("setoption name PolyglotFile value C:/polyglot-collection/komodo.bin");
            out.println("setoption name SyzygyPath value C:/books/syzygy/3-4-5");

            // isready command
            out.println("isready");
            assertEquals("readyok", in.readLine());

            // ucinewgame command
            out.println("ucinewgame");

            // isready command
            out.println("isready");
            assertEquals("readyok", in.readLine());

            // isrpositioneady command
            out.println("position startpos");

            // quit command
            out.println("quit");

            while (uciMain.isRunning()) {
                Thread.sleep(200);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
