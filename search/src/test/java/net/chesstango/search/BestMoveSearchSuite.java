package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.search.reports.SearchesReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase esta destinada a resolver test-positions
 * <p>
 * https://www.chessprogramming.org/Test-Positions
 *
 * @author Mauricio Coria
 */
public class BestMoveSearchSuite {

    private static final int DEFAULT_MAX_DEPTH = 5;

    public static void main(String[] args) {
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\failed-2023-04-30.epd");

        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Bratko-Kopec\\Bratko-Kopec.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\wac\\wac-2018.epd");

        /*
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS1.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS2.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS3.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS4.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS5.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS6.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS7.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS8.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS9.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS10.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS11.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS12.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS13.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS14.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS15.epd");
         */
    }

    protected static void execute(String filename) {
        EPDReader reader = new EPDReader();
        List<EPDReader.EDPEntry> edpEntries = reader.readEdpFile(filename);
        BestMoveSearchSuite suite = new BestMoveSearchSuite(DEFAULT_MAX_DEPTH);
        suite.run(filename, edpEntries);
        System.gc();
    }

    protected final int depth;
    protected final List<SearchMoveResult> searchMoveResults;

    public BestMoveSearchSuite(int depth) {
        this.depth = depth;
        this.searchMoveResults = new ArrayList<>();
    }

    protected void run(String suiteName, List<EPDReader.EDPEntry> edpEntries) {
        List<String> failedSuites = new ArrayList<String>();

        for (EPDReader.EDPEntry edpEntry : edpEntries) {
            if (run(edpEntry) == false) {
                failedSuites.add(edpEntry.fen);
            }
        }

        System.out.println("Suite summary " + suiteName);
        if (failedSuites.isEmpty()) {
            System.out.println("\t all tests executed successfully");
        } else {
            for (String suiteStr : failedSuites) {
                System.out.println("\t test failed: " + suiteStr);
            }
        }
        System.out.printf("Success rate: %d%% \n", (100 * (edpEntries.size() - failedSuites.size())) / edpEntries.size());

        new SearchesReport()
                //.withCutoffStatics()
                .withNodesVisitedStatics()
                .withExportEvaluations()
                .printSearchesStatics(searchMoveResults);

        System.out.println("=================");
    }

    protected boolean run(EPDReader.EDPEntry edpEntry) {
        SearchMove moveFinder = new DefaultSearchMove();

        SearchMoveResult searchResult = moveFinder.search(edpEntry.game, depth);

        Move bestMove = searchResult.getBestMove();

        boolean result = edpEntry.bestMoves.contains(bestMove);

        if (result) {
            System.out.printf("Success %s\n", edpEntry.fen);
        } else {
            SANEncoder sanEncoder = new SANEncoder();
            System.out.printf("Fail '%s', expected %s, best move found %s \n", edpEntry.text,
                    edpEntry.bestMovesString,
                    sanEncoder.encode(bestMove, edpEntry.game.getPossibleMoves()));
        }

        searchMoveResults.add(searchResult);

        return result;
    }

}
