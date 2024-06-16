package net.chesstango.tools;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.tools.perft.Perft;
import net.chesstango.tools.perft.PerftResult;
import net.chesstango.tools.perft.imp.PerftBrute;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author Mauricio Coria
 */
public class PerftMain {

    private static List<String> fenTested = new ArrayList<>();

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(Thread.currentThread()));

        try (PrintStream out = new PrintStream(
                new FileOutputStream("./PerftMainTestSuiteResult.txt", false))) {

            execute("/main/ferdy_perft_double_checks.epd", out);
            execute("/main/ferdy_perft_enpassant_1.epd", out);

            execute("/main/ferdy_perft_single_check_1.epd", out);
            execute("/main/ferdy_perft_single_check_2.epd", out);
            execute("/main/ferdy_perft_single_check_3.epd", out);
            execute("/main/ferdy_perft_single_check_4.epd", out);
            execute("/main/ferdy_perft_single_check_5.epd", out);
            execute("/main/ferdy_perft_single_check_6.epd", out);
            execute("/main/ferdy_perft_single_check_7.epd", out);
            execute("/main/ferdy_perft_single_check_8.epd", out);
            execute("/main/ferdy_perft_single_check_9.epd", out);
            execute("/main/ferdy_perft_single_check_10.epd", out);
            execute("/main/ferdy_perft_single_check_11.epd", out);
            execute("/main/ferdy_perft_single_check_12.epd", out);
            execute("/main/ferdy_perft_single_check_13.epd", out);
            execute("/main/ferdy_perft_single_check_14.epd", out);
            execute("/main/ferdy_perft_single_check_15.epd", out);
            execute("/main/ferdy_perft_single_check_16.epd", out);
            execute("/main/ferdy_perft_single_check_17.epd", out);
            execute("/main/ferdy_perft_single_check_18.epd", out);
            execute("/main/ferdy_perft_single_check_19.epd", out);

            execute("/main/perft_suite0.epd", out);
            execute("/main/perft_suite1.epd", out);
            execute("/main/perft_suite2.epd", out);
            execute("/main/perft_suite3.epd", out);

            execute("/main/perft-marcel.epd", out);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execute(String filename, PrintStream out) {
        try {
            System.out.println("Starting Test suite " + filename);
            out.println("Starting Test suite " + filename);

            List<String> failedSuites = new ArrayList<String>();
            List<String> duplicatedSuites = new ArrayList<String>();

            InputStream instr = PerftMain.class.getResourceAsStream(filename);

            // reading the files with buffered reader
            InputStreamReader inputStreamReader = new InputStreamReader(instr);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int availableCores = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(availableCores);

            List<Future<PerftMain>> futures = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
                    PerftMain suite = new PerftMain();
                    suite.parseTests(line);
                    String currentFen = suite.getFen();
                    if (!fenTested.contains(currentFen)) {
                        fenTested.add(currentFen);
                        futures.add(executorService.submit(() -> {
                            suite.run();
                            return suite;
                        }));
                    } else {
                        duplicatedSuites.add(currentFen);
                    }
                }
            }

            executorService.shutdown();
            try {
                while (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    int totalTasks = futures.size();
                    int completed = 0;
                    for (Future<PerftMain> future : futures) {
                        if (future.isDone()) {
                            completed++;
                        }
                    }
                    System.out.println("Completed: " + completed + "/" + totalTasks);
                }
            } catch (InterruptedException e) {
                System.out.println("Stopping executorService....");
                executorService.shutdownNow();
            }

            int testExcecuted = 0;
            int testPending = 0;
            for (Future<PerftMain> future : futures) {
                if (future.isDone()) {
                    testExcecuted++;
                    PerftMain suite = future.get();
                    if (!suite.isResult()) {
                        failedSuites.add(suite.getFen());
                    }
                } else {
                    testPending++;
                }
            }

            System.out.println("Suite summary " + filename);
            out.println("Suite summary " + filename);

            System.out.println("\t Tests executed: " + testExcecuted);
            out.println("\t Tests executed: " + testExcecuted);

            if (testPending > 0) {
                System.out.println("\t Tests pending: " + testPending);
                out.println("\t Tests pending: " + testPending);
            }

            if (failedSuites.isEmpty()) {
                System.out.println("\t all tests executed successfully");
                out.println("\t all tests executed successfully");
            } else {
                System.out.println("\t Tests executed failed: " + failedSuites.size());
                out.println("\t Tests executed failed: " + failedSuites.size());
                Collections.sort(failedSuites);
                for (String suiteStr : failedSuites) {
                    System.out.println("\t test executed failed: " + suiteStr);
                    out.println("\t test executed failed: " + suiteStr);
                }
            }

            Collections.sort(duplicatedSuites);
            for (String suiteStr : duplicatedSuites) {
                System.out.println("\t Test duplicated (not executed): " + suiteStr);
                out.println("\t Test duplicated (not executed): " + suiteStr);
            }
            System.out.println("=================");
        } catch (Exception e) {
            System.out.println(e);
            out.println(e);
        }
        out.flush();
    }

    private static class ShutdownHook extends Thread {

        private final Thread mainThread;

        private ShutdownHook(Thread mainThread) {
            this.mainThread = mainThread;
        }


        @Override
        public void run() {
            System.out.println("Shutting down....");
            mainThread.interrupt();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Getter
    private String fen;
    @Getter
    private boolean result = false;
    protected long[] expectedPerftResults;
    private int startLevel;
    private PrintStream out = System.out;

    protected boolean run(String perfTest) {
        parseTests(perfTest);
        return run();
    }

    protected boolean run() {
        Boolean returnResult = null;
        try {
            out.println(Thread.currentThread().getName() + ">> " + "Testing FEN: " + this.fen);
            for (int i = 0; i < expectedPerftResults.length; i++) {

                Perft main = createPerft();

                PerftResult result = main.start(getGame(), this.startLevel + i);

                if (result.getTotalNodes() == expectedPerftResults[i]) {
                    out.println(Thread.currentThread().getName() + ">> " + "depth " + (this.startLevel + i) + " OK");
                } else {
                    out.println(Thread.currentThread().getName() + ">> " + "depth " + (this.startLevel + i) + " FAIL, expected = " + expectedPerftResults[i] + ", actual = " + result.getTotalNodes());
                    returnResult = false;
                    break;
                }

            }
            if (returnResult == null) {
                returnResult = true;
            }
            out.println(Thread.currentThread().getName() + ">> " + "=============");
        } catch (Exception e) {
            e.printStackTrace();
            out.println(Thread.currentThread().getName() + ">> " + e);
        }

        this.result = returnResult;
        return returnResult;
    }

    protected void parseTests(String tests) {
        String[] splitStrings = tests.split(";");

        this.fen = splitStrings[0].trim();
        this.expectedPerftResults = new long[splitStrings.length - 1];
        this.startLevel = 0;

        for (int i = 1; i < splitStrings.length; i++) {
            String[] perftResultStr = splitStrings[i].trim().split(" ");
            if (this.startLevel == 0) {
                this.startLevel = Integer.parseInt(perftResultStr[0].substring(1));
            }
            expectedPerftResults[i - 1] = Long.parseLong(perftResultStr[1]);
        }

    }

    private Game getGame() {
        GameBuilder builder = new GameBuilder(new ChessFactory());

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(this.fen);

        return builder.getChessRepresentation();
    }


    protected Perft createPerft() {
        //return new PerftWithMap<Long>(PerftWithMap::getZobristGameId);
        return new PerftBrute();
    }
}
