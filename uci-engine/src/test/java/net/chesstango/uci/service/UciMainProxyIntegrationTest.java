
package net.chesstango.uci.service;

import net.chesstango.uci.proxy.UciProxy;
import net.chesstango.uci.proxy.ProxyConfig;
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
public class UciMainProxyIntegrationTest {

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
    public void test_playProxy() throws IOException, InterruptedException {
        List<String> lines = null;

        PipedOutputStream outputToEngine = new PipedOutputStream();
        PipedInputStream inputFromEngine = new PipedInputStream();

        UciProxy engine = new UciProxy(ProxyConfig.loadEngineConfig("Spike"))
                .setLogging(false);

        UciMain uciMain = new UciMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine), true));
        executorService.submit(uciMain::run);

        PrintStream out = new PrintStream(outputToEngine, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

        // uci command
        out.println("uci");
        Thread.sleep(200);
        lines = readLastLine(in, "uciok"::equals);
        assertTrue(lines.stream().filter("id name Spike 1.4"::equals).findAny().isPresent());

        // isready command
        out.println("isready");
        Thread.sleep(200);
        assertEquals("readyok", in.readLine());

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
        assertTrue(lines.size() > 0);

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
