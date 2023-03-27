package net.chesstango.search;

import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.SANEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BestMoveFinderSuite {

    private static final int MAX_DEPTH = 6;

    public static void main(String[] args) {
        execute("main/ferdy_perft_double_checks.epd");
    }

    private static void execute(String filename) {
        List<String> failedSuites = new ArrayList<String>();
        BestMoveFinderSuite suite = new BestMoveFinderSuite(MAX_DEPTH);

        EDPReader reader = new EDPReader();
        List<EDPReader.EDPEntry> edpEntries = reader.readEdpFile(filename);

        for (EDPReader.EDPEntry edpEntry: edpEntries) {
            if (suite.run(edpEntry) == false) {
                failedSuites.add(edpEntry.fen);
            }
        }

        System.out.println("Suite summary " + filename);
        if (failedSuites.isEmpty()) {
            System.out.println("\t all tests exceute sucessfully");
        } else {
            for (String suiteStr : failedSuites) {
                System.out.println("\t test failed: " + suiteStr);
            }
        }
        System.out.println("=================");
    }

    private final ChessFactory chessFactory;
    private final int depth;

    public BestMoveFinderSuite(int depth) {
        this(new ChessFactory(), depth);
    }

    public BestMoveFinderSuite(ChessFactory chessFactory, int depth) {
        this.chessFactory = chessFactory;
        this.depth = depth;
    }

    protected boolean run(EDPReader.EDPEntry edpEntry) {
        SearchMove moveFinder = new DefaultSearchMove();

        SearchMoveResult searchResult = moveFinder.searchBestMove(edpEntry.game, depth);

        SANEncoder sanEncoder = new SANEncoder();

        //System.out.printf("Best moves %s \n", searchResult.getBestMoveOptions().stream().map(move -> sanEncoder.encode(move, edpEntry.game.getPossibleMoves())).collect(Collectors.toList()));

        Move bestMove = searchResult.getBestMove();

        return bestMove.equals(edpEntry.expectedMove);
    }

}
